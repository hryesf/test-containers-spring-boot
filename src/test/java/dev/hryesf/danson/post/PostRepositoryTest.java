package dev.hryesf.danson.post;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withReuse(true);

    @Mock
    PostRepository postRepository;

    @BeforeEach
    void setUp() {
        List<Post> posts = List.of(new Post(1, 1, "Hello, world!", "this is my first post.", null));
        postRepository.saveAll(posts);
    }

    @BeforeAll
    static void setup() {
        postgres.start();
    }

    @AfterAll
    static void tearDown() {
        postgres.close();
        postgres.stop();
    }

    @Test
    void connectionEstablished() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    void shouldReturnPostsByTitle() {
        Optional<Post> post = postRepository.findByTitle("Hello, world!");
        assertThat(post).isNotNull();
    }

}