package org.tbk.bitcoin.tool.fee.blockstreaminfo;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class BlockstreamInfoFeeApiClientImplTest {
    private static final String BASE_URL = "https://blockstream.info";
    private static final String API_TOKEN = "test";

    private BlockstreamInfoFeeApiClientImpl sut;

    @Before
    public void setUp() {
        this.sut = new BlockstreamInfoFeeApiClientImpl(BASE_URL, API_TOKEN);
    }

    @Test
    public void itShouldGetFeeEstimates() {
        FeeEstimates feeEstimates = this.sut.feeEstimates();

        assertThat(feeEstimates, is(notNullValue()));

        List<FeeEstimates.Entry> entries = feeEstimates.getEntryList();
        assertThat(entries, hasSize(greaterThan(0)));
    }
}
