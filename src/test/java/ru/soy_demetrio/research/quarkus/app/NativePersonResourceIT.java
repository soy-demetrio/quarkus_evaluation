package ru.soy_demetrio.research.quarkus.app;

import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
public class NativePersonResourceIT extends PersonResourceTest {

    // Execute the same tests but in native mode.
}