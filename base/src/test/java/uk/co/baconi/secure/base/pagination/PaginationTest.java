package uk.co.baconi.secure.base.pagination;

import org.junit.Test;
import uk.co.baconi.secure.base.BaseUnitTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PaginationTest extends BaseUnitTest {

    @Test
    public void shouldBeAbleToCreatePagination() {

        final Pagination pagination1 = new Pagination(10, 11, 12L);
        assertThat(pagination1.getPage(), is(equalTo(10)));
        assertThat(pagination1.getPerPage(), is(equalTo(11)));
        assertThat(pagination1.getTotalCount(), is(equalTo(12L)));

        final Pagination pagination2 = new Pagination(20, 21, 22L);
        assertThat(pagination2.getPage(), is(equalTo(20)));
        assertThat(pagination2.getPerPage(), is(equalTo(21)));
        assertThat(pagination2.getTotalCount(), is(equalTo(22L)));
    }

    @Test
    public void shouldHaveNiceToStringRepresentation() {

        final Pagination pagination = new Pagination(0, 2, 5L);

        final String paginationAsString = pagination.toString();

        assertThat(paginationAsString, containsString("page=0,"));
        assertThat(paginationAsString, containsString("perPage=2,"));
        assertThat(paginationAsString, containsString("totalCount=5}"));
    }
}
