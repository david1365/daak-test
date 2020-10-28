package ir.daakit.unittest.file;

import ir.daakit.unittest.annotations.DisableMockStatic;
import ir.daakit.unittest.annotations.MockAutowired;
import ir.daakit.unittest.annotations.MockModel;
import ir.daakit.unittest.annotations.MockStatic;
import ir.daakit.unittest.fortest.MockClass;
import ir.daakit.unittest.fortest.MockStringClass;
import ir.daakit.unittest.model.SampleModel;
import ir.daakit.unittest.model.SampleModelWithList;
import ir.daakit.unittest.runner.PerfectSpringRunner;
import com.google.gson.Gson;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author Davood Akbari - 1399
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@RunWith(PerfectSpringRunner.class)
@ContextConfiguration
public class SpringJsonModelTest extends TestCase {
    @MockModel
    private SampleModel actualSampleModelAnnotation;

    @MockModel
    private List<SampleModelWithList> actualSampleModelAnnotationList2List;

    private SampleModel expectSampleModel;

    private List<SampleModel> expectSampleModelList;

    private List<SampleModelWithList> expectSampleModelAnnotationList2List;

    @MockAutowired
    private MockClass mockClass;

    @MockStatic
    private MockedStatic<MockClass> mockStaticClass;

    @MockStatic
    private MockedStatic<MockStringClass> mockStringStaticClass;

    @Configuration
    static class Config {

        @Bean
        public Gson gson() {
            return new Gson();
        }
    }

    @Before
    public void setup() {
        expectSampleModel = new SampleModel();
        expectSampleModel.setId(1);
        expectSampleModel.setName("davood");

        expectSampleModelList = new ArrayList<SampleModel>() {{
            add(expectSampleModel);
        }};

        final SampleModelWithList sampleModelWithList = new SampleModelWithList();
        sampleModelWithList.setId(1);
        sampleModelWithList.setSampleModelList(expectSampleModelList);

        expectSampleModelAnnotationList2List = new ArrayList<SampleModelWithList>() {{
            add(sampleModelWithList);
        }};
    }

    @Test
    public void testReadJsonAnnotationList2List() {
        assertEquals(expectSampleModelAnnotationList2List, actualSampleModelAnnotationList2List);
    }

    @Test
    public void testMockAutowired() {
        when(mockClass.create(any(Long.class))).thenReturn(actualSampleModelAnnotation);

        SampleModel actualSampleModelAnnotationMockClass = mockClass.create(123L);
        assertEquals(expectSampleModel, actualSampleModelAnnotationMockClass);
    }

    @Test
    public void testMockAutowiredReturnNull() {
        when(mockClass.create(any(Long.class))).thenReturn(null);

        SampleModel actualSampleModelAnnotationMockClass = mockClass.create(123L);
        assertEquals(null, actualSampleModelAnnotationMockClass);
    }

    @Test
    public void testMockStaticReturnNull() {
        long expectedLongNumber = 2_000_000L;
        mockStaticClass.when(MockClass::testStatic).thenReturn(expectedLongNumber);

        long actualNumber = MockClass.testStatic();
        assertEquals(expectedLongNumber, actualNumber);

        String expectedString = "Muhammad Hossein";
        mockStringStaticClass.when(() -> MockStringClass.testStringStatic(anyString())).thenReturn(expectedString);

        String actualString = MockStringClass.testStringStatic("");
        assertEquals(expectedString, actualString);
    }

    @Test
    @DisableMockStatic(MockClass.class)
    public void testMockStaticReturnActualNumber() {
        long actualLongNumber = MockClass.testStatic();
        assertEquals(MockClass.actualNumber, actualLongNumber);
    }

    @Test
    @DisableMockStatic({MockClass.class, MockStringClass.class})
    public void testMockStaticReturnActualName() {
        String actualName = MockStringClass.testStringStatic("");
        assertEquals(MockStringClass.actualName, actualName);
    }
}