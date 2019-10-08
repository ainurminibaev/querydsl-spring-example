package com.technaxis.querydsl.utils;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * @author Dmitry Sadchikov
 */
public class StringJoiner {

    private final String delimiter;

    /*
     * StringBuilder value -- at any time, the characters constructed from the
     * prefix, the added element separated by the delimiter, but without the
     * suffix, so that we can more easily add elements without having to jigger
     * the suffix each time.
     */
    private StringBuilder value;

    /*
     * By default, the string consisting of prefix+suffix, returned by
     * toString(), or properties of value, when no elements have yet been added,
     * i.e. when it is empty.  This may be overridden by the user to be some
     * other value including the empty String.
     */
    private String emptyValue;

    /**
     * Constructs a {@code StringJoiner} with no characters in it, with no
     * {@code prefix} or {@code suffix}, and a copy of the supplied
     * {@code delimiter}.
     * If no characters are added to the {@code StringJoiner} and methods
     * accessing the value of it are invoked, it will not return a
     * {@code prefix} or {@code suffix} (or properties thereof) in the result,
     * unless {@code setEmptyValue} has first been called.
     *
     * @param  delimiter the sequence of characters to be used between each
     *         element added to the {@code StringJoiner} value
     * @throws NullPointerException if {@code delimiter} is {@code null}
     */
    private StringJoiner(CharSequence delimiter) {
        Objects.requireNonNull(delimiter, "The delimiter must not be null");
        this.delimiter = delimiter.toString();
        this.emptyValue = "";
    }

    /**
     * Constructs a {@code StringJoiner} with no characters in it, with no
     * {@code prefix} or {@code suffix}, and a copy of the supplied
     * {@code delimiter}.
     * If no characters are added to the {@code StringJoiner} and methods
     * accessing the value of it are invoked, it will not return a
     * {@code prefix} or {@code suffix} (or properties thereof) in the result,
     * unless {@code setEmptyValue} has first been called.
     *
     * @param  delimiter the sequence of characters to be used between each
     *         element added to the {@code StringJoiner} value
     * @throws NullPointerException if {@code delimiter} is {@code null}
     */
    public static StringJoiner with(CharSequence delimiter) {
        return new StringJoiner(delimiter);
    }

    public static StringJoiner withSlash() {
        return new StringJoiner("/");
    }

    public static StringJoiner withSpace() {
        return new StringJoiner(StringUtils.SPACE);
    }

    /**
     * Sets the sequence of characters to be used when determining the string
     * representation of this {@code StringJoiner} and no elements have been
     * added yet, that is, when it is empty.  A copy of the {@code emptyValue}
     * parameter is made for this purpose. Note that once an add method has been
     * called, the {@code StringJoiner} is no longer considered empty, even if
     * the element(s) added correspond to the empty {@code String}.
     *
     * @param  emptyValue the characters to return as the value of an empty
     *         {@code StringJoiner}
     * @return this {@code StringJoiner} itself so the calls may be chained
     * @throws NullPointerException when the {@code emptyValue} parameter is
     *         {@code null}
     */
    public StringJoiner setEmptyValue(CharSequence emptyValue) {
        this.emptyValue = Objects.requireNonNull(emptyValue,
                "The empty value must not be null").toString();
        return this;
    }

    /**
     * Returns the current value, consisting of the {@code prefix}, the values
     * added so far separated by the {@code delimiter}, and the {@code suffix},
     * unless no elements have been added in which case, the
     * {@code prefix + suffix} or the {@code emptyValue} characters are returned
     *
     * @return the string representation of this {@code StringJoiner}
     */
    @Override
    public String toString() {
        if (value == null) {
            return emptyValue;
        } else {
            return value.toString();
        }
    }

    /**
     * Adds a copy of the given {@code CharSequence} value as the next
     * element of the {@code StringJoiner} value. If {@code newElement} is
     * {@code null}, then {@code "null"} is added.
     *
     * @param  newElement The element to add
     * @return a reference to this {@code StringJoiner}
     */
    public StringJoiner add(CharSequence newElement) {
        prepareBuilder().append(newElement);
        return this;
    }

    /**
     * Adds a {@code String} representation of the given {@code Object} value as the next
     * element of the {@code StringJoiner} value. If {@code obj} is
     * {@code null}, then {@code "null"} is added.
     *
     * @param  obj The element to add
     * @return a reference to this {@code StringJoiner}
     */
    public StringJoiner add(@Nullable Object obj) {
        prepareBuilder().append(obj);
        return this;
    }

    /**
     * Adds a copy of the given {@code CharSequence} value as the next
     * element of the {@code StringJoiner} value. If {@code newElement} is
     * {@code null}, then nothing will be added.
     *
     * @param  newElement The element to add
     * @return a reference to this {@code StringJoiner}
     */
    public StringJoiner addIfPresent(@Nullable CharSequence newElement) {
        if (newElement != null) {
            prepareBuilder().append(newElement);
        }
        return this;
    }

    /**
     * Adds a {@code String} representation of the given {@code Object} value as the next
     * element of the {@code StringJoiner} value. If {@code obj} is
     * {@code null}, then nothing will be added.
     *
     * @param  obj The element to add
     * @return a reference to this {@code StringJoiner}
     */
    public StringJoiner addIfPresent(@Nullable Object obj) {
        if (obj != null) {
            prepareBuilder().append(obj);
        }
        return this;
    }

    /**
     * Adds a {@code String} representation of the given {@code data} array as the next
     * element of the {@code StringJoiner} value.
     *
     * @param  data The array to add
     * @return a reference to this {@code StringJoiner}
     */
    public StringJoiner add(char[] data) {
        prepareBuilder().append(data);
        return this;
    }

    /**
     * Adds a {@code String} representation of the given {@code b} value as the next
     * element of the {@code StringJoiner} value.
     *
     * @param  b The element to add
     * @return a reference to this {@code StringJoiner}
     */
    public StringJoiner add(boolean b) {
        prepareBuilder().append(b);
        return this;
    }

    /**
     * Adds a {@code String} representation of the given {@code c} value as the next
     * element of the {@code StringJoiner} value.
     *
     * @param  c The element to add
     * @return a reference to this {@code StringJoiner}
     */
    public StringJoiner add(char c) {
        prepareBuilder().append(c);
        return this;
    }

    /**
     * Adds a {@code String} representation of the given {@code i} value as the next
     * element of the {@code StringJoiner} value.
     *
     * @param  i The element to add
     * @return a reference to this {@code StringJoiner}
     */
    public StringJoiner add(int i) {
        prepareBuilder().append(i);
        return this;
    }

    /**
     * Adds a {@code String} representation of the given {@code l} value as the next
     * element of the {@code StringJoiner} value.
     *
     * @param  l The element to add
     * @return a reference to this {@code StringJoiner}
     */
    public StringJoiner add(long l) {
        prepareBuilder().append(l);
        return this;
    }

    /**
     * Adds a {@code String} representation of the given {@code f} value as the next
     * element of the {@code StringJoiner} value.
     *
     * @param  f The element to add
     * @return a reference to this {@code StringJoiner}
     */
    public StringJoiner add(float f) {
        prepareBuilder().append(f);
        return this;
    }

    /**
     * Adds a {@code String} representation of the given {@code d} value as the next
     * element of the {@code StringJoiner} value.
     *
     * @param  d The element to add
     * @return a reference to this {@code StringJoiner}
     */
    public StringJoiner add(double d) {
        prepareBuilder().append(d);
        return this;
    }

    /**
     * Adds the contents of the given {@code StringJoiner} without prefix and
     * suffix as the next element if it is non-empty. If the given {@code
     * StringJoiner} is empty, the call has no effect.
     *
     * <p>A {@code StringJoiner} is empty if {@link #add(CharSequence) add()}
     * has never been called, and if {@code merge()} has never been called
     * with a non-empty {@code StringJoiner} argument.
     *
     * <p>If the other {@code StringJoiner} is using a different delimiter,
     * then elements from the other {@code StringJoiner} are concatenated with
     * that delimiter and the result is appended to this {@code StringJoiner}
     * as a single element.
     *
     * @param other The {@code StringJoiner} whose contents should be merged
     *              into this one
     * @throws NullPointerException if the other {@code StringJoiner} is null
     * @return This {@code StringJoiner}
     */
    public StringJoiner merge(StringJoiner other) {
        Objects.requireNonNull(other);
        if (other.value != null) {
            final int length = other.value.length();
            // lock the length so that we can seize the data to be appended
            // before initiate copying to avoid interference, especially when
            // merge 'this'
            StringBuilder builder = prepareBuilder();
            builder.append(other.value, 0, length);
        }
        return this;
    }

    /**
     * Cleans value of {@code StringJoiner}
     *
     * @return a reference to this {@code StringJoiner}
     */
    public StringJoiner clear() {
        this.value = null;
        return this;
    }

    private StringBuilder prepareBuilder() {
        if (value != null) {
            value.append(delimiter);
        } else {
            value = new StringBuilder();
        }
        return value;
    }

    /**
     * Returns the length of the {@code String} representation
     * of this {@code StringJoiner}. Note that if
     * no add methods have been called, then the length of the {@code String}
     * representation (either {@code prefix + suffix} or {@code emptyValue})
     * will be returned. The value should be equivalent to
     * {@code toString().length()}.
     *
     * @return the length of the current value of {@code StringJoiner}
     */
    public int length() {
        // Remember that we never actually append the suffix unless we return
        // the full (present) value or some sub-string or length of it, so that
        // we can add on more if we need to.
        return (value != null ? value.length() : emptyValue.length());
    }
}
