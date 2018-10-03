package com.gvjay.classmanager;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.gvjay.classmanager.Database.ClassObject;
import com.gvjay.classmanager.Database.DBHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DBTest {

    private DBHelper helper;

    @Before
    public void setUp() {
        this.helper = new DBHelper(InstrumentationRegistry.getTargetContext());

        ClassObject classObject = new ClassObject("Tuesday", 2, 60*60*1000*4, 60*60*1000*5);
        ClassObject classObject2 = new ClassObject("Wednesday 1", 3, 60*60*1000*4, 60*60*1000*5);
        ClassObject classObject3 = new ClassObject("Wednesday 2", 3, 60*60*1000*5, 60*60*1000*6);
        this.helper.addClass(classObject);
        this.helper.addClass(classObject3);
        this.helper.addClass(classObject2);
    }

    @After
    public void cleanUp() {
        this.helper.clearDB();
    }

    @Test
    public void testDBHelper() {
        ArrayList<ClassObject> classObjects = this.helper.getClassesOnDay(2);

        assertThat(classObjects.size(), is(1));
        assertThat(classObjects.get(0).title, is("Tuesday"));

        classObjects = this.helper.getClassesOnDay(3);

        assertThat(classObjects.size(), is(2));
        assertThat(classObjects.get(0).title, is("Wednesday 1"));
        assertThat(classObjects.get(1).title, is("Wednesday 2"));
    }

}
