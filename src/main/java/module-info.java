module uk.ac.leeds.ccg.data {
    requires transitive java.logging;
    requires java.desktop;
    requires uk.ac.leeds.ccg.generic;
    requires uk.ac.leeds.ccg.math;
    exports uk.ac.leeds.ccg.data;
    exports uk.ac.leeds.ccg.data.converter;
    exports uk.ac.leeds.ccg.data.core;
    exports uk.ac.leeds.ccg.data.format;
    exports uk.ac.leeds.ccg.data.id;
    exports uk.ac.leeds.ccg.data.interval;
    exports uk.ac.leeds.ccg.data.io;
}