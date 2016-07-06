package debop4k.core.collections.eclipseCollections;

import kotlin.Pair;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.list.mutable.primitive.DoubleArrayList;
import org.eclipse.collections.impl.list.mutable.primitive.FloatArrayList;
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList;
import org.eclipse.collections.impl.list.mutable.primitive.LongArrayList;
import org.eclipse.collections.impl.map.mutable.UnifiedMap;
import org.eclipse.collections.impl.set.mutable.UnifiedSet;
import org.junit.Test;

import java.util.List;

import static debop4k.core.collections.EclipseCollectionsKt.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author sunghyouk.bae@gmail.com
 */
public class EclipseCollectionExtensionsTest {

  @Test
  public void testArrayListOf() {
    IntArrayList intArrayList = intArrayListOf(1, 2, 3, 4, 5);
    assertThat(intArrayList.size()).isEqualTo(5);
    assertThat(intArrayList.contains(4)).isTrue();
    assertThat(intArrayList.contains(0)).isFalse();

    LongArrayList longArrayList = longArrayListOf(1, 2, 3, 4, 5);
    assertThat(longArrayList.size()).isEqualTo(5);
    assertThat(longArrayList.contains(4)).isTrue();
    assertThat(longArrayList.contains(0)).isFalse();

    FloatArrayList floatArrayList = floatArrayListOf(1, 2, 3, 4, 5);
    assertThat(floatArrayList.size()).isEqualTo(5);
    assertThat(floatArrayList.contains(4)).isTrue();
    assertThat(floatArrayList.contains(0)).isFalse();

    DoubleArrayList doubleArrayList = doubleArrayListOf(1, 2, 3, 4, 5);
    assertThat(doubleArrayList.size()).isEqualTo(5);
    assertThat(doubleArrayList.contains(4)).isTrue();
    assertThat(doubleArrayList.contains(0)).isFalse();
  }

  @Test
  public void testAsList() {
    List<Integer> ints = asList(intArrayListOf(1, 2, 3, 4, 5));
    assertThat(ints).hasSize(5).contains(1, 2, 3, 4, 5);

    List<Long> longs = asList(longArrayListOf(1, 2, 3, 4, 5));
    assertThat(longs).hasSize(5).contains(1L, 2L, 3L, 4L, 5L);
  }

  @Test
  public void testFastListOf() {
    FastList<Object> empty = fastListOf();
    assertThat(empty).isEmpty();

    FastList<Integer> ints = fastListOf(1, 2, 3, 4, 5);
    assertThat(ints).isNotEmpty().contains(1, 2, 3, 4, 5);
  }

  @Test
  public void testUnifiedSetOf() {
    UnifiedSet<Integer> set = unifiedSetOf(1, 2, 3, 3, 3);
    assertThat(set).hasSize(3).contains(1, 2, 3);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testUnifiedMapOf() {
    UnifiedMap<Integer, String> map = unifiedMapOf(new Pair(1, "a"), new Pair(2, "b"), new Pair(3, "c"));
    assertThat(map.size()).isEqualTo(3);
    assertThat(map.get(1)).isEqualTo("a");
    assertThat(map.get(2)).isEqualTo("b");
    assertThat(map.get(3)).isEqualTo("c");
  }
}
