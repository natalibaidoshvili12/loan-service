package ge.mziuri.loanservice.external;

import ge.mziuri.loanservice.exception.BookNotFoundException;
import ge.mziuri.loanservice.exception.NoAvailableCopiesException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class BookClient {

    private final RestTemplate restTemplate;

    private final String BOOK_SERVICE_URL = "http://localhost:8081/api/books";

    public void reserve(Long bookId) {
        String url = BOOK_SERVICE_URL + "/" + bookId + "/reserve";

        try {
            restTemplate.postForEntity(url, null, Void.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new BookNotFoundException(
                    "Book with ID " + bookId + " not found"
            );
        } catch (HttpClientErrorException.Conflict e) {
            throw new NoAvailableCopiesException(
                    "No available copies for book ID " + bookId
            );
        }
    }

    public void release(Long bookId) {
        String url = BOOK_SERVICE_URL + "/" + bookId + "/release";

        try {
            restTemplate.postForEntity(url, null, Void.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new BookNotFoundException(
                    "Book with ID " + bookId + " not found"
            );
        }
    }
}