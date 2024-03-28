package dev.hryesf.danson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.asm.TypeReference;
import org.springframework.boot.CommandLineRunner;

import javax.imageio.IIOException;
import java.io.IOException;
import java.io.InputStream;

public class PostDataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(PostDataLoader.class);
    private final ObjectMapper mapper;
    private final PostRepository postRepository;

    public PostDataLoader(ObjectMapper mapper, PostRepository postRepository) {
        this.mapper = mapper;
        this.postRepository = postRepository;
    }

    public void run(String... args) throws Exception {
        if(postRepository.count() == 0){
            String POSTS_JSON = "/data/posts.json";
            log.info("Loading posts into database from {}", POSTS_JSON);
            try (InputStream inputStream = TypeReference.class.getResourceAsStream(POSTS_JSON)){
                Posts response = mapper.readValue(inputStream, Posts.class);
                postRepository.saveAll(response.posts());
            }catch (IOException e){
                throw new RuntimeException("Failed to load posts from " + POSTS_JSON, e);
            }
        }
    }
}
