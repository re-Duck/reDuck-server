package reduck.reduck.domain.image;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reduck.reduck.global.entity.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class ImageController {

    @GetMapping("/C:/{storage}/{type}/{userId}/{name}")
    public ResponseEntity<Resource> getImage(@PathVariable String storage,@PathVariable String type, @PathVariable String userId,@PathVariable String name) throws IOException {
        Path path = Paths.get("C:/" + storage + "/" +type+ "/" +userId+ "/" +name);
        String contentType = Files.probeContentType(path);

        HttpHeaders headers = new HttpHeaders();
        Resource resource = new InputStreamResource(Files.newInputStream(path));

        headers.add(HttpHeaders.CONTENT_TYPE, contentType);
//        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
    @GetMapping("/home/ubuntu/reduck/storage/{type}/{userId}/{name}")
    public ResponseEntity<Resource> getProdImage(@PathVariable String type, @PathVariable String userId,@PathVariable String name) throws IOException {
        Path path = Paths.get("/home/ubuntu/reduck/storage" + "/" +type+ "/" +userId+ "/" +name);
        String contentType = Files.probeContentType(path);

        HttpHeaders headers = new HttpHeaders();
        Resource resource = new InputStreamResource(Files.newInputStream(path));

        headers.add(HttpHeaders.CONTENT_TYPE, contentType);
//        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
