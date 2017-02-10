/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2017 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import org.junit.Test;

import java.math.BigDecimal;

public abstract class AbstractSetTest extends AbstractTraversableRangeTest {

    @Override
    abstract protected <T> Set<T> empty();

    abstract protected <T> Set<T> emptyWithNull();

    @Override
    abstract protected <T> Set<T> of(T element);

    @SuppressWarnings("unchecked")
    @Override
    abstract protected <T> Set<T> of(T... elements);

    // -- static narrow

    @Test
    public void shouldNarrowSet() {
        final Set<Double> doubles = of(1.0d);
        final Set<Number> numbers = Set.narrow(doubles);
        final int actual = numbers.add(new BigDecimal("2.0")).sum().intValue();
        assertThat(actual).isEqualTo(3);
    }

    // -- add

    @Test
    public void shouldAddNullAndNonNull() {
        assertThat(emptyWithNull().add(null).add(1)).contains(null, 1);
    }

    @Test
    public void shouldAddNonNullAndNull() {
        assertThat(emptyWithNull().add(1).add(null)).contains(null, 1);
    }

    @Test
    public void shouldNotAddAnExistingElementTwice() {
        final Set<IntMod2> set = of(new IntMod2(2));
        assertThat(set.add(new IntMod2(4))).isSameAs(set);
    }

    // -- addAll

    @Test
    public void shouldAddAllOfIterable() {
        assertThat(of(1, 2, 3).addAll(of(2, 3, 4))).isEqualTo(of(1, 2, 3, 4));
    }

    @Test
    public void shouldReturnSameSetWhenAddAllEmptyToNonEmpty() {
        final Set<Integer> set = of(1, 2, 3);
        assertThat(set.addAll(empty())).isSameAs(set);
    }

    @Test
    public void shouldReturnSameSetWhenAddAllNonEmptyToEmpty() {
        final Set<Integer> set = of(1, 2, 3);
        if (set.isOrdered()) {
            assertThat(empty().addAll(set)).isEqualTo(set);
        } else {
            assertThat(empty().addAll(set)).isSameAs(set);
        }
    }

    @Test
    public void shouldReturnSameSetWhenAddAllContainedElements() {
        final Set<Integer> set = of(1, 2, 3);
        assertThat(set.addAll(of(1, 2, 3))).isSameAs(set);
    }

    // -- diff

    @Test
    public void shouldCalculateDifference() {
        assertThat(of(1, 2, 3).diff(of(2))).isEqualTo(of(1, 3));
        assertThat(of(1, 2, 3).diff(of(5))).isEqualTo(of(1, 2, 3));
        assertThat(of(1, 2, 3).diff(of(1, 2, 3))).isEqualTo(empty());
    }

    @Test
    public void shouldReturnSameSetWhenEmptyDiffNonEmpty() {
        final Set<Integer> empty = empty();
        assertThat(empty.diff(of(1, 2))).isSameAs(empty);
    }

    @Test
    public void shouldReturnSameSetWhenNonEmptyDiffEmpty() {
        final Set<Integer> set = of(1, 2);
        assertThat(set.diff(empty())).isSameAs(set);
    }

    // -- intersect

    @Test
    public void shouldCalculateIntersect() {
        assertThat(of(1, 2, 3).intersect(of(2))).isEqualTo(of(2));
        assertThat(of(1, 2, 3).intersect(of(5))).isEqualTo(empty());
        assertThat(of(1, 2, 3).intersect(of(1, 2, 3))).isEqualTo(of(1, 2, 3));
    }

    @Test
    public void shouldReturnSameSetWhenEmptyIntersectNonEmpty() {
        final Set<Integer> empty = empty();
        assertThat(empty.intersect(of(1, 2))).isSameAs(empty);
    }

    @Test
    public void shouldReturnSameSetWhenNonEmptyIntersectEmpty() {
        final Set<Integer> set = of(1, 2);
        final Set<Integer> empty = empty();
        if (set.isOrdered()) {
            assertThat(set.intersect(empty)).isEqualTo(empty);
        } else {
            assertThat(set.intersect(empty)).isSameAs(empty);
        }
    }

    // -- map

    @Test
    public void shouldMapDistinctElementsToOneElement() {
        assertThat(of(1, 2, 3).map(i -> 0)).isEqualTo(of(0));
    }

    // -- remove

    @Test
    public void shouldRemoveElement() {
        assertThat(of(1, 2, 3).remove(2)).isEqualTo(of(1, 3));
        assertThat(of(1, 2, 3).remove(5)).isEqualTo(of(1, 2, 3));
        assertThat(empty().remove(5)).isEqualTo(empty());
    }

    // -- removeAll

    @Test
    public void shouldRemoveAllElements() {
        assertThat(of(1, 2, 3).removeAll(of(2))).isEqualTo(of(1, 3));
        assertThat(of(1, 2, 3).removeAll(of(5))).isEqualTo(of(1, 2, 3));
    }

    @Test
    public void shouldReturnSameSetWhenNonEmptyRemoveAllEmpty() {
        final Set<Integer> set = of(1, 2, 3);
        assertThat(set.removeAll(empty())).isSameAs(set);
    }

    @Test
    public void shouldReturnSameSetWhenEmptyRemoveAllNonEmpty() {
        final Set<Integer> empty = empty();
        assertThat(empty.removeAll(of(1, 2, 3))).isSameAs(empty);
    }

    // -- union

    @Test
    public void shouldCalculateUnion() {
        assertThat(of(1, 2, 3).union(of(2))).isEqualTo(of(1, 2, 3));
        assertThat(of(1, 2, 3).union(of(5))).isEqualTo(of(1, 2, 3, 5));
        assertThat(of(1, 2, 3).union(of(1, 2, 3))).isEqualTo(of(1, 2, 3));
    }

    @Test
    public void shouldReturnSameSetWhenEmptyUnionNonEmpty() {
        final Set<Integer> set = of(1, 2);
        if (set.isOrdered()) {
            assertThat(empty().union(set)).isEqualTo(set);
        } else {
            assertThat(empty().union(set)).isSameAs(set);
        }
    }
    
    @Test
    public void shouldReturnSameSetWhenNonEmptyUnionEmpty() {
        final Set<Integer> set = of(1, 2);
        assertThat(set.union(empty())).isSameAs(set);
    }

    // disabled tests

    @Override
    @Test
    public void shouldBeAwareOfExistingNonUniqueElement() {
        // sets have only distinct elements
    }

    @Override
    @Test
    public void shouldReplaceFirstOccurrenceOfNonNilUsingCurrNewWhenMultipleOccurrencesExist() {
        // sets have only distinct elements
    }
}
