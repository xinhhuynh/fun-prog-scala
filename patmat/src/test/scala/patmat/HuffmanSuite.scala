package patmat

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import patmat.Huffman._

@RunWith(classOf[JUnitRunner])
class HuffmanSuite extends FunSuite {
  trait TestTrees {
    val t1 = Fork(Leaf('a',2), Leaf('b',3), List('a','b'), 5)
    val t2 = Fork(Fork(Leaf('a',2), Leaf('b',3), List('a','b'), 5), Leaf('d',4), List('a','b','d'), 9)
  }

  test("weight of a larger tree") {
    new TestTrees {
      assert(weight(t1) === 5)
    }
  }

  test("chars of a larger tree") {
    new TestTrees {
      assert(chars(t2) === List('a','b','d'))
    }
  }

  test("string2chars(\"hello, world\")") {
    assert(string2Chars("hello, world") === List('h', 'e', 'l', 'l', 'o', ',', ' ', 'w', 'o', 'r', 'l', 'd'))
  }

  test("times") {
    assert(times(List('a', 'b', 'a')) == List(('a', 2), ('b', 1)))
  }
  
  test("makeOrderedLeafList") {
    assert(makeOrderedLeafList(List(('a', 2), ('b', 1))) == List(Leaf('b', 1), Leaf('a', 2)))
  }
  
  test("makeOrderedLeafList for some frequency table") {
    assert(makeOrderedLeafList(List(('t', 2), ('e', 1), ('x', 3))) === List(Leaf('e',1), Leaf('t',2), Leaf('x',3)))
  }

  test("singleton") {
    new TestTrees {
      assert(singleton(List(t1)) == true)
      assert(singleton(List(t1, t2)) == false)
    }
  }
  
  test("combine of some leaf list") {
    val leaflist = List(Leaf('e', 1), Leaf('t', 2), Leaf('x', 4))
    assert(combine(leaflist) === List(Fork(Leaf('e',1),Leaf('t',2),List('e', 't'),3), Leaf('x',4)))
  }

  test("until") {
    val leaflist = List(Leaf('e', 2), Leaf('t', 3), Leaf('x', 4))
    assert(until(singleton, combine)(leaflist) == List(makeCodeTree(Leaf('x', 4),makeCodeTree(Leaf('e', 2), Leaf('t', 3)))))
  }
  
  test("createCodeTree") {
    new TestTrees {
      assert(createCodeTree(List('b', 'a', 'b', 'a', 'b')) == t1)
      assert(createCodeTree(List('b', 'a', 'd', 'd', 'b', 'd', 'a', 'd', 'b')) == 
        makeCodeTree(Leaf('d', 4), makeCodeTree(Leaf('a', 2), Leaf('b', 3))) )
    }
  }
  
  test("decode") {
    new TestTrees {
      assert(decode(t1, List(1)) == List('b'))
      assert(decode(t2, List(0, 1, 0, 0, 1)) == List('b', 'a', 'd'))
    }
  }
  
  test("decoded secret") {
    assert(decodedSecret == List('h','u','f','f','m','a','n','e','s','t','c','o','o','l'))
  }
  
  test("decode and encode a very short text should be identity") {
    new TestTrees {
      assert(decode(t1, encode(t1)("ab".toList)) === "ab".toList)
    }
  }
  
  test("codeBits") {
    assert( codeBits( List(('a', List(0, 0)), ('b', List(0, 1)), ('d', List(1))) )('b') == List(0, 1) )
  }
  
  test("convert") {
    new TestTrees {
      assert(convert(t2) == List(('a', List(0, 0)), ('b', List(0, 1)), ('d', List(1))))    
    }
  }
  
  test("quick encode") {
    new TestTrees {
      assert(decode(t1, quickEncode(t1)("ab".toList)) === "ab".toList)
    }
  }
}
