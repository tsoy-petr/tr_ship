package com.example.demo.utils;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

/**
 * Wraps a given <code>Format</code> and overrides that <code>null</code>
 * values are formatted to the empty string.<p>
 *
 * <strong>Examples:</strong><pre>
 * new DisplayFormat(DateFormat.getDateInstance());
 * new DisplayFormat(new NumberFormat());
 * new DisplayFormat(new NumberFormat(), true);
 * </pre>
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.5 $
 */

public final class DisplayFormat extends Format {

    /**
     * Refers to the wrapped Format that is used to forward
     * <code>#format</code>.
     */
    private final Format delegate;

    /**
     * Describes whether the value's class is part of formatted output.
     */
    private final boolean showClass;


    // Instance Creation ****************************************************

    /**
     * Constructs an <code>EmptyFormat</code> that wraps the given format
     * to convert <code>null</code> to the empty string and vice versa.
     *
     * @param format  the format that handles the standard cases
     */
    public DisplayFormat(Format format) {
        this(format, false);
    }

    /**
     * Constructs an <code>EmptyFormat</code> that wraps the given format
     * to convert <code>null</code> to the empty string and vice versa.
     *
     * @param format    the format that handles the standard cases
     * @param showClass true to format the value's class
     */
    public DisplayFormat(Format format, boolean showClass) {
        this.delegate  = format;
        this.showClass = showClass;
    }


    // Implementing Abstract Behavior ***************************************

    /**
     * Formats an object and appends the resulting text to a given string
     * buffer. If the <code>pos</code> argument identifies a field used by
     * the format, then its indices are set to the beginning and end of
     * the first such field encountered.
     *
     * @param obj    The object to format
     * @param toAppendTo    where the text is to be appended
     * @param pos    A <code>FieldPosition</code> identifying a field
     *               in the formatted text
     * @return       the string buffer passed in as <code>toAppendTo</code>,
     *               with formatted text appended
     * @exception NullPointerException if <code>toAppendTo</code> or
     *            <code>pos</code> is null
     * @exception IllegalArgumentException if the Format cannot format the given
     *            object
     */
    public StringBuffer format(Object obj, StringBuffer toAppendTo,
                               FieldPosition pos) {
        if (obj == null)
            toAppendTo.append(obj);
        else {
            delegate.format(obj, toAppendTo, pos);
            if (showClass) {
                toAppendTo.append(" (");
                toAppendTo.append(shortClassName(obj));
                toAppendTo.append(')');
            }
        }
        return toAppendTo;
    }

    private String shortClassName(Object object) {
        String fullClassName = object.getClass().getName();
        int dotIndex = fullClassName.lastIndexOf('.');
        return dotIndex == -1
                ? fullClassName
                : fullClassName.substring(dotIndex + 1, fullClassName.length());
    }


    /**
     * Parses text from a string to produce an object.
     *
     * @param source A <code>String</code>, part of which should be parsed.
     * @param pos A <code>ParsePosition</code> object with index and error
     *            index information as described above.
     * @return An <code>Object</code> parsed from the string. In case of
     *         error, returns null.
     * @exception NullPointerException if <code>pos</code> is null.
     */
    public Object parseObject(String source, ParsePosition pos) {
        throw new UnsupportedOperationException("Display format only.");
    }

}
