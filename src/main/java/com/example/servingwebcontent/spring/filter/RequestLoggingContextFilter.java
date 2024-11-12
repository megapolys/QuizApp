package com.example.servingwebcontent.spring.filter;

import com.example.servingwebcontent.properties.LoggerProperties;
import com.example.servingwebcontent.spring.servlet.http.CachedBodyHttpServletRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.example.servingwebcontent.consts.Headers.*;
import static com.example.servingwebcontent.consts.Mask.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestLoggingContextFilter extends OncePerRequestFilter {

	private final LoggerProperties properties;

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain
	) throws ServletException, IOException {
		if (!isAsyncDispatch(request)) {
			request = printRequestInfo(request);
		}
		filterChain.doFilter(request, response);
	}

	private HttpServletRequest printRequestInfo(HttpServletRequest request) {
		ThreadContext.put(METHOD, request.getMethod());
		ThreadContext.put(REMOTE_ADD, request.getRemoteAddr());
		ThreadContext.put(REMOTE_HOST, request.getRemoteHost());
		ThreadContext.put(REQUEST_URI, request.getRequestURI());

		String queryString = request.getQueryString();
		if (queryString == null) {
			ThreadContext.put(REQUEST_URI, request.getRequestURI());
		} else {
			ThreadContext.put(REQUEST_URI, request.getRequestURI() + "?" + queryString);
		}

		if (log.isInfoEnabled()) {
			try {
				request = new CachedBodyHttpServletRequest(request);
				StringBuilder builder = new StringBuilder().append(System.lineSeparator());
				builder.append(request.getMethod()).append(" ").append(request.getRequestURI());

				if (StringUtils.isNotBlank(queryString)) {
					builder.append("?").append(queryString);
				}

				boolean showFull = true;
				if (request.getRequestURI() != null && CollectionUtils.isNotEmpty(properties.getIgnoreURIPrefix())) {
					showFull = properties.getIgnoreURIPrefix().stream()
							.noneMatch(request.getRequestURI()::startsWith);
				}

				if (showFull) {
					builder.append(System.lineSeparator()).append("body: ");
					boolean bodyVisible = true;
					if (request.getContentType() != null) {
						MediaType mediaType = MediaType.valueOf(request.getContentType());
						bodyVisible = properties.getIgnoreContentTypes().stream().noneMatch(ignoreType -> ignoreType.includes(mediaType));
					}

					if (bodyVisible) {
						if (request.getContentLength() == 0) {
							builder.append("{}");
						} else if (request.getContentLength() > properties.getMaxBodySizeInByte()) {
							builder.append("{ *body.length > maxBodySizeInByte* }");
						} else {
							byte[] content = ((CachedBodyHttpServletRequest) request).getCachedBody();
							String requestBody = maskValues(new String(content));
							builder.append(requestBody);
						}
					} else {
						builder.append("{ *invisible* }");
					}

					HttpServletRequest requestForHeader = request;
					builder.append(System.lineSeparator()).append("headers: {");
					Iterator<String> iterator = requestForHeader.getHeaderNames().asIterator();
					while (iterator.hasNext()) {
						String name = iterator.next();
						if (!properties.getIgnoreHeaders().contains(name)) {
							builder.append(System.lineSeparator()).append("    ").append(name).append(": ").append(requestForHeader.getHeader(name));
						}
					}
					builder.append(System.lineSeparator()).append("}");
				}
				log.info(builder.toString());
			} catch (Throwable thr) {
				logger.warn("Cannot print request" + thr.getLocalizedMessage(), thr);
			}
		}
		return request;
	}

	protected String maskValues(String message) {
		if (CollectionUtils.isNotEmpty(properties.getMaskValueKeys())) {
			String pattern = properties.getMaskValueKeys().stream()
					.map(key -> key + PATTERN_END + "|" + key + PATTERN_ARRAY_END)
					.collect(Collectors.joining("|"));
			Matcher matcher = Pattern.compile(pattern).matcher(message);
			StringBuilder sb = new StringBuilder(message);
			int idx = 0;
			while (matcher.find(idx)) {
				for (int group = 1; group <= matcher.groupCount(); group++) {
					if (matcher.group(group) != null) {
						sb.replace(matcher.start(group), matcher.end(group), REPLACE_STRING);
						idx = matcher.start(group) + REPLACE_STRING.length() + 1;
					}
				}
				matcher.reset(sb.toString());
			}
			return sb.toString();
		} else {
			return message;
		}
	}
}
