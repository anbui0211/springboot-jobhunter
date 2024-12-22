package vn.andev.jobhunter.util;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import jakarta.servlet.http.HttpServletResponse;
import vn.andev.jobhunter.domain.RestResponse;

@ControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice<Object> {
    // "ResponseBodyAdvice" sẽ chạy trước, tiếp đó mới chạy vào "GlobalException"

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        // We only want to apply this advice to REST responses
        // Khi nào muốn ghi đè, khi nào muốn format phản hồi API
        // Muốn bất cứ phản hồi nào cũng ghi đè => "return true"
        // Muốn check if-else phụ thuộc vào controller thì có thể viết trong hàm này
        // "return false" => không format, "return true" => tiếp tục chạy xuống
        // beforeBodyWrite bên dưới
        return true;
    }

    @Override
    @Nullable
    public Object beforeBodyWrite(
            @Nullable Object body, // Data trả về client
            MethodParameter returnType,
            MediaType selectedContentType,
            Class selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {
        // Http status code (lấy ra httpCode trả về client)
        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int status = servletResponse.getStatus();

        RestResponse<Object> res = new RestResponse<Object>(); // Body response phải là kiểu object
        res.setStatusCode(status);

        // if you want to return string response
        // if (body instanceof String) {
        // return body;
        // }

        if (status >= 400) {
            // case error
            return body;
        } else {
            // case success
            res.setMessage("CALL API SUCCESS");
            res.setData(body);
        }

        return res;
    }
}