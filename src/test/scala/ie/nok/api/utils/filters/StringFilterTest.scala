package ie.nok.api.utils.filters

import ie.nok.stores.filters.StringFilter as Filter
import ie.nok.stores.filters.StringFilter.{ContainsCaseInsensitive, EqualsCaseInsensitive}

class StringFilterTest extends munit.FunSuite {
  test("build StringFilter.in") {
    val stringFilterIn = StringFilter(contains = None, equals = None, in = Some(List("a", "b", "c")))
    val obtained       = StringFilter.toInternal(stringFilterIn)
    val expected       = Filter.Or(EqualsCaseInsensitive("a"), EqualsCaseInsensitive("b"), EqualsCaseInsensitive("c"))
    assertEquals(obtained, expected)
  }
  test("build StringFilter.equals") {
    val stringFilterIn = StringFilter(contains = None, equals = Some("a"), in = None)
    val obtained       = StringFilter.toInternal(stringFilterIn)
    val expected       = EqualsCaseInsensitive("a")
    assertEquals(obtained, expected)
  }
  test("build StringFilter.contains") {
    val stringFilterIn = StringFilter(contains = Some("b"), equals = None, in = None)
    val obtained       = StringFilter.toInternal(stringFilterIn)
    val expected       = ContainsCaseInsensitive("b")
    assertEquals(obtained, expected)
  }
}
