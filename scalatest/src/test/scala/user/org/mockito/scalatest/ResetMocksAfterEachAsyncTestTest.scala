package user.org.mockito.scalatest

import org.mockito.MockitoSugar
import org.mockito.scalatest.ResetMocksAfterEachAsyncTest
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec

import scala.concurrent.Future

class ResetMocksAfterEachAsyncTestTest extends AsyncWordSpec with MockitoSugar with ResetMocksAfterEachAsyncTest with Matchers {
  trait Foo {
    def bar(a: String) = Future.successful("bar")
  }

  trait Baz {
    def qux(a: String) = Future.successful("qux")
  }

  val foo: Foo = mock[Foo]
  val baz: Baz = mock[Baz]

  "ResetMocksAfterEachAsyncTest" should {
    "have clean state for test 1" in {
      verifyZeroInteractions(foo)

      when(foo.bar("pepe")) thenReturn Future.successful("mocked")

      foo.bar("pepe").map { f =>
        f shouldBe "mocked"
      }
    }

    "have clean state for test 2" in {
      verifyZeroInteractions(foo)

      when(foo.bar("pepe")) thenReturn Future.successful("mocked2")

      foo.bar("pepe").map { f =>
        f shouldBe "mocked2"
      }
    }

    "have clean state for all mocks test 1" in {
      verifyZeroInteractions(foo, baz)

      when(foo.bar("pepe")) thenReturn Future.successful("mocked3")
      when(baz.qux("epep")) thenReturn Future.successful("mocked4")

      Future.sequence(List(foo.bar("pepe"), baz.qux("epep"))).map { f =>
        f should contain theSameElementsAs List("mocked3", "mocked4")
      }
    }

    "have clean state for all mocks test 2" in {
      verifyZeroInteractions(foo, baz)

      when(foo.bar("pepe")) thenReturn Future.successful("mocked5")
      when(baz.qux("epep")) thenReturn Future.successful("mocked6")

      Future.sequence(List(foo.bar("pepe"), baz.qux("epep"))).map { f =>
        f should contain theSameElementsAs List("mocked5", "mocked6")
      }
    }
  }
}
