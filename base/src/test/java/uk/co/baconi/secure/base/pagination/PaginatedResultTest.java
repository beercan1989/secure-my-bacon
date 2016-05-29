package uk.co.baconi.secure.base.pagination;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import uk.co.baconi.secure.base.BaseUnitTest;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PaginatedResultTest extends BaseUnitTest {

    @Mock
    private Page<String> data;

    @Test
    public void shouldBeAbleToCreatePaginatedResult() {

        reset(data);
        when(data.getContent()).thenReturn(Arrays.asList("first", "second"));
        when(data.getNumber()).thenReturn(0);
        when(data.getSize()).thenReturn(2);
        when(data.getTotalElements()).thenReturn(5L);

        final PaginatedResult<String> paginatedResult1 = new PaginatedResult<>(data);
        assertThat(paginatedResult1.getData(), contains("first", "second"));
        assertThat(paginatedResult1.getPaging().getPage(), is(equalTo(0)));
        assertThat(paginatedResult1.getPaging().getPerPage(), is(equalTo(2)));
        assertThat(paginatedResult1.getPaging().getTotalCount(), is(equalTo(5L)));

        reset(data);
        when(data.getContent()).thenReturn(Arrays.asList("third", "fourth", "fifth"));
        when(data.getNumber()).thenReturn(1);
        when(data.getSize()).thenReturn(3);
        when(data.getTotalElements()).thenReturn(6L);

        final PaginatedResult<String> paginatedResult2 = new PaginatedResult<>(data);
        assertThat(paginatedResult2.getData(), contains("third", "fourth", "fifth"));
        assertThat(paginatedResult2.getPaging().getPage(), is(equalTo(1)));
        assertThat(paginatedResult2.getPaging().getPerPage(), is(equalTo(3)));
        assertThat(paginatedResult2.getPaging().getTotalCount(), is(equalTo(6L)));
    }

    @Test
    public void shouldHaveNiceToStringRepresentation() {

        reset(data);
        when(data.getContent()).thenReturn(Arrays.asList("first", "second"));
        when(data.getNumber()).thenReturn(0);
        when(data.getSize()).thenReturn(2);
        when(data.getTotalElements()).thenReturn(5L);

        final PaginatedResult<String> paginatedResult = new PaginatedResult<>(data);

        final String paginatedResultAsString = paginatedResult.toString();

        assertThat(paginatedResultAsString, containsString("data=[first, second],"));
        assertThat(paginatedResultAsString, containsString("paging=Pagination("));
    }
}
