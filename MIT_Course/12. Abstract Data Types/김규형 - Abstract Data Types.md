# ****Abstract Data Types(23.10.04)****

## Objects

- 추상 자료형
- 표현 독립성
- 추상 데이터 유형의 위험한 문제를 해결하고, 클라이언트가 내부 표현에 대한 가정이 왜 위험하고, 어떻게 그것을 피할 수 있는가?
- 연산의 분류와 추상적인 데이터 유형에 대한 좋은 설계 원리를 세우는법

## What Abstraction Means

- 추상화 데이터 유형은 소프트웨어 공학의 일반적인 원리 중 하나
- 다양한 의미로 이야기 되어진다.
    - **추상화** : 낮은 정보를 숨기고 더 단순하게 표현
    - **모듈화** : 시스템을 모듈로 나누고, 각 구성 요소에 설계, 구현 등의 기능을 추가
    - **캡슐화** : 모듈 주위에 벽을 만들어 모듈 내부가 각자 스스로에게 책임을 부여, 다른 부분의 무결성 보장
    - **정보 숨김** : 모듈의 세부 정보를 숨겨 추후 시스템 변경시 다른 모듈을 변경할 필요가 없음
    - **관심사 분리** : 기능을 만들때는 하나의 모듈에만 책임을 남긴다.
- 소프트웨어 공학에서는 이러한 용어를 잘 아는 것이 중요하다.

### User-Defined Types

- 컴퓨팅 시대의 초기에는 내장된 타입과 프로시저들로 input과 output를 조절하였다.
- 이러한 하나의 프로시저를 정의하기 위해 거대한 프로그램의 제작에 들어갔다.
- 사용자 정의 유형을 허용하는 것은 상당히 진보적인 아이디어
- Dahi(Simula 언어 개발자), Hoare(현재 사용죽인 추상화 타입의 발명), Parnas(캡슐화와 정보 숨기기 등의 정의) 등등이 제안
- MIT에서 추상타입의 명세에 대해 개발
- 데이터 추상화의 핵심 아이디어는 어떤 동작에 따라 유형이 특정화 된다는 것
- 숫자는 더하고 곱하고, 문자열은 연결 가능하는것 불리언은 참 거짓을 구별하는 등…
- 다른 의미로 프로그래머는 초기 프로그래밍언어로 자신의 유형을 정의가 가능하다.
- 예를 들어 년/월/일을 나타내는 날짜형
- 하지만 계산을 못함 → 이게 추상화의 핵심
- 사용자가 저장되는 방식에 벗어나 자유롭게 저장하고 계산하는 것.
- 자바에서는 내장형 타입과 사용자 정의형이 구분된다.
- `Integer` 나 `boolean` 은 `java.lang` 에 존재 하지만 `java.util` 을 기본으로 제공하는 건지와는 덜 명확하다.
- 이는 자바가 여전히 객체가 아닌 원시형의 데이터를 가지고 있음으로써 나타나는 문제
    - `int` 와 `boolean` 은 사용자가 확장이 불가능하다…

## Classifiying Types and Operations

- 내장형이던 사용자 정의 형이던 확실하게 분류할 수 있는건 **mutable** 인가 **immutable** 로 분류 된다.
- mutable한 객체는 변경 될 수 있다. 즉 실행될 때 다른 작업을 할 경우 다른 결과를 기대할 수 있다.
- `Date` 는 mutable 하다. 그렇기에 `setMonth` 로 변경을 가지고 `getMonth` 로 이것을 관찰 할 수 있다.
- `String` 은 immutable 하다. 하지만 `StringBuiler` 는 mutable 하다.
- 추상 유형의 연산은 다음과 같이 분류된다.
    - **Creators** : 새 객체를 만든다. 객체의 인자로 활용 될 순 있지만, 구성되어진 객체는 사용할 수 없다.
    - **Producers** : 오래된 객체로 새 객체를 만든다. `String` 의 `concat()` 매소드가 대표적
        - `concat()` : 두 문자열을 연결하여 새로운 String 객체 생성
    - **Observers** : 추상 객체의 정보를 다른 객체에 return 해준다 `size` 메소드가 대표적
    - **Mutators** : 객체를 바꾼다. `add` 가 대표적
- 이러한 연산을 요약한다면
    - creator: t* → T
    - producer: T+, t* → T
    - observer: T+, t* → t
    - mutator: T+, t* → void|t|T
- 다양한 class 들의 연산 signatures를 보여준다
- **T** 는 추상적 유형 그 자체로 각각 다른 타입이다. + 는 한번이상 발생을, * 는 0 번 이상 발생을 말한다.
- creator operation은 생성사로 구현되는 경우가 많다. `new ArrayList()` 처럼.
- 그러나 creator는 단순히 static method가 될 수는 없는데 `Arrays.asList()` 가 대표적이다.
- 이런 creator는 종종 **Factory method** 라고도 불린다. `String.valueOf` 가 대표적인 factory method 중 하나.
- Mutators는 종종 `void` return type에 불린다. 아무것도 반환하지 않은 것을 여러가지 부작용을 동반 할수 잇다. `boolean` 을 활용해 정상적으로 변경되었는지 알리는 부울을 반환라면 돌연변이를 줄일 수있다.

### Abstract Data Type Examples

- 추상 데이터 유형과 몇가지 작업의 유형
- `int` 는 원시형 타입이고 immutable 하다.
    - creators: the numeric literals 0, 1, 2, …
    - producers: arithmetic operators +, -, ×, ÷
    - observers: comparison operators ==, !=, <, >
    - mutators: none (it’s immutable)
- `List` 는 mutable 하다.
    - creators: `ArrayList` and `LinkedList` constructors, `[Collections.singletonList](http://docs.oracle.com/javase/8/docs/api/java/util/Collections.html#singletonList-T-)`
    - producers: `[Collections.unmodifiableList](http://docs.oracle.com/javase/8/docs/api/java/util/Collections.html#unmodifiableList-T-)`
    - observers: `size`, `get`
    - mutators: `add`, `remove`, `addAll`, `[Collections.sort](http://docs.oracle.com/javase/8/docs/api/java/util/Collections.html#sort-java.util.List-)`
- `String` 은 immutable
    - creators: `String` constructors
    - producers: `concat`, `substring`, `toUpperCase`
    - observers: `length`, `charAt`
    - mutators: none (it’s immutable)

## Designing an Abstract Type

- 추상적 유형을 설계하는데에는 좋은 작업을 선택하고 어떻게 동작을 가져야하는지에 대한 결정이 필요하다.
- 복잡한 작업 하나보다는 쉬운 작업 몇개가 좋다는것을 명심하자
- 각각의 작동은 잘 정의된 목적을 가져야 하며 일관된 행동을 가져야한다 ( 애매해서는 안된다. )
- 만약에 멋대로 `List` 에 `sum` 을 추가한다면 단순히 Integers, String, nested 등등 모든 케이스에 대해서 강력하게 적용시켜야한다.
- 각 연산 집합은 다양한 상황에서 충분히 적합해야한다.
- 좋은 테스트는 모든 객체에 대해서 확인하는 것이다.
- 예를들어 `get` 연산자가 없다면 우리는 리스트의 요소를 알아낼수 없다.
- 기본적인 정보를 얻어내기에 어렵지 않아야함을 명시하자.
- 또다른 예로 `size` 연산자는 indices가 실패할때까지 증가 하는 식으로 알아낼수 있지만 이건 매우 비 효율적이다.
- 타입은 집합일 수도 그래프 일수도 있다.
- 하지만 일반적인 기능과 도메인별 기능을 혼동해서는 안된다.

## ****Representation Independence****

- 좋은 추상 데이터 타입은 표현 독립적이여야한다.
- 추상 타입이 실제 사용자의 표현(실제 데이터 구조) 등과 독립적이므로 표현의 변화가 외부 코드에 영향을 미치지 않게 한다는것을 의미
- 예를 들어 List가 연결된 list인지 배열인지는 중요하지 않다.
- ADT의 표현은 전제 조건과 사후 조건을 완전히 지정하지 않은 한 변경해서는 안된다.
- 고객은 이러한 조건을 확실하게 알고 의존관계를 파악한 후에 변경여부를 파악한다.

### ****Example: Different Representations for Strings****

- ****Representation Independence****가 정확히 무엇을 의미하는지 예시
- 실제 `String` 을 표현한 `MyString` 코드

```java
/** MyString represents an immutable sequence of characters. */
public class MyString {

    //////////////////// Example of a creator operation ///////////////
    /** @param b a boolean value
     *  @return string representation of b, either "true" or "false" */
    public static MyString valueOf(boolean b) { ... }

    //////////////////// Examples of observer operations ///////////////
    /** @return number of characters in this string */
    public int length() { ... }

    /** @param i character position (requires 0 <= i < string length)
     *  @return character at position i */
    public char charAt(int i) { ... }

    //////////////////// Example of a producer operation ///////////////
    /** Get the substring between start (inclusive) and end (exclusive).
     *  @param start starting index
     *  @param end ending index.  Requires 0 <= start <= end <= string length.
     *  @return string consisting of charAt(start)...charAt(end-1) */
    public MyString substring(int start, int end) { ... }
}
```

- 이러한 연산자와 명세는 오직 사용자가 알수 있는 유일한 정보
- 이러한 자료형에 대해 테스트를 시도한다.
- 하지만 `assertEquals` 는 시도할 수 없다?
    - 왜? equailty를 정의하지 않았기에 → equailty를 어떻게 신중하게 구현할지도 추후 공부
- 현재로썬 구현되어 있는 5가지 메소드에 대해서만 테스트 한다.
- `valueOf` 테스트
    
    ```java
    MyString s = MyString.valueOf(true);
    assertEquals(4, s.length());
    assertEquals('t', s.charAt(0));
    assertEquals('r', s.charAt(1));
    assertEquals('u', s.charAt(2));
    assertEquals('e', s.charAt(3));
    ```
    
    - 이것에 대한 테스트는 마지막에..
- `MyString` 에 대한 표현에 있어 `char` 배열은 정확히 string의 길이이다. 끝에 추가적인 공간은 있으면 안된다.
- 내부 표현은 다음과 같이 선언되어 있다.
    - `private char[] a;`
- 이러한 표현을 선택한다면 연산자의 구현은 다음과 같을 것이다.
    
    ```java
    public static MyString valueOf(boolean b) {
        MyString s = new MyString();
        s.a = b ? new char[] { 't', 'r', 'u', 'e' }
                : new char[] { 'f', 'a', 'l', 's', 'e' };
        return s;
    }
    
    public int length() {
        return a.length;
    }
    
    public char charAt(int i) {
        return a[i];
    }
    
    public MyString substring(int start, int end) {
        MyString that = new MyString();
        that.a = new char[end - start];
        System.arraycopy(this.a, start, that.a, 0, end - start);
        return that;
    }
    ```
    
- 이러한 구현의 문제점은 성능 개선의 기회가 없다는 것이다.
- 데이터 타입이 immutable이기에 굳이 `subString` 에서 새로운 문자열로 복사할 필요가 없다.
- 기존에 존재하는 문자열의 시작과 끝을 추적하는 방식으로 성능 개선의 여지가 있다.
- 최적화를 구현하기 위해 내부 표현을 변경
    
    ```java
    private char[] a;
    private int start;
    private int end;
    ```
    
- 새로운 연산자 구현
    
    ```java
        public static MyString valueOf(boolean b) {
            MyString s = new MyString();
            s.a = b ? new char[] { 't', 'r', 'u', 'e' }
                    : new char[] { 'f', 'a', 'l', 's', 'e' };
            s.start = 0;
            s.end = s.a.length;
            return s;
        }
    
        public int length() {
            return end - start;
        }
    
        public char charAt(int i) {
          return a[start + i];
        }
    
        public MyString substring(int start, int end) {
            MyString that = new MyString();
            that.a = this.a;
            that.start = this.start + start;
            that.end = this.start + end;
            return that;
        }
    ```
    
- 이러한 구현의 변경은 `MyString` 이 개인의 영역이 아닌 오직 공개된 명세에만 의존하기에 바로 변경이 가능하다. 이것이 표현 독립성의 힘

## ****Realizing ADT Concepts in Java****

- 우리는 ADT 아이디어를 통해 Java 언어 기능을 사용하여 공부해보았다
- 자바 언어에 존재하는 ADT 개념들은 다음과 같다

| ADT concept | Ways to do it in Java | Examples |
| --- | --- | --- |
| Creator operation | Constructor | http://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html#ArrayList-- |
|  | Static (factory) method | http://docs.oracle.com/javase/8/docs/api/java/util/Collections.html#singletonList-T-, http://docs.oracle.com/javase/8/docs/api/java/util/Arrays.html#asList-T...- |
|  | Constant | http://docs.oracle.com/javase/8/docs/api/java/math/BigInteger.html#ZERO |
| Observer operation | Instance method | http://docs.oracle.com/javase/8/docs/api/java/util/List.html#get-int- |
|  | Static method | http://docs.oracle.com/javase/8/docs/api/java/util/Collections.html#max-java.util.Collection- |
| Producer operation | Instance method | http://docs.oracle.com/javase/8/docs/api/java/lang/String.html#trim-- |
|  | Static method | http://docs.oracle.com/javase/8/docs/api/java/util/Collections.html#unmodifiableList-java.util.List- |
| Mutator operation | Instance method | http://docs.oracle.com/javase/8/docs/api/java/util/List.html#add-E- |
|  | Static method | http://docs.oracle.com/javase/8/docs/api/java/util/Collections.html#copy-java.util.List-java.util.List- |
| Representation | private fields |  |

## Summary

- 추상 데이터 타입은 그들의 연산에 따라 특정되어진다.
- 좋은 ADT는 일괄적이며 적절하고, 간단하며 표현 독립적이다.
- ADT의 명세는 연산자의 집합과 그들의 스펙이다.
- 연산자는 creators, producers, observers 그리고 mutators로 분리된다.
- 좋은 ADT는 명확한 명세를 제공하기에 잠재적 버그를 줄이고 간단한 연산 뒤에 구현을 숨겨 이해하기 쉽게 하며 표현독립성으로 변화에 준비 되었다.

## ****Testing an Abstract Data Type****

- ADT에 대한 테스트 케이스는 서로와 상호작용한다.
- creators, producers, mutators 같은 경우는 실제 값을 관찰하여 테스트 하는게 가장 효율적이다.
- input Test를 분할한 경우
    
    ```java
    // testing strategy for each operation of MyString:
    //
    // valueOf():
    //    true, false
    // length():
    //    string len = 0, 1, n
    //    string = produced by valueOf(), produced by substring()
    // charAt():
    //    string len = 1, n
    //    i = 0, middle, len-1
    //    string = produced by valueOf(), produced by substring()
    // substring():
    //    string len = 0, 1, n
    //    start = 0, middle, len
    //    end = 0, middle, len
    //    end-start = 0, n
    //    string = produced by valueOf(), produced by substring()
    ```
    
- 이를 바탕으로 test suite는 다음과 같다.
    
    ```java
    @Test public void testValueOfTrue() {
        MyString s = MyString.valueOf(true);
        assertEquals(4, s.length());
        assertEquals('t', s.charAt(0));
        assertEquals('r', s.charAt(1));
        assertEquals('u', s.charAt(2));
        assertEquals('e', s.charAt(3));
    }
    
    @Test public void testValueOfFalse() {
        MyString s = MyString.valueOf(false);
        assertEquals(5, s.length());
        assertEquals('f', s.charAt(0));
        assertEquals('a', s.charAt(1));
        assertEquals('l', s.charAt(2));
        assertEquals('s', s.charAt(3));
        assertEquals('e', s.charAt(4));
    }
    
    @Test public void testEndSubstring() {
        MyString s = MyString.valueOf(true).substring(2, 4);
        assertEquals(2, s.length());
        assertEquals('u', s.charAt(0));
        assertEquals('e', s.charAt(1));
    }
    
    @Test public void testMiddleSubstring() {
        MyString s = MyString.valueOf(false).substring(1, 2);
        assertEquals(1, s.length());
        assertEquals('a', s.charAt(0));
    }
    
    @Test public void testSubstringIsWholeString() {
        MyString s = MyString.valueOf(false).substring(0, 5);
        assertEquals(5, s.length());
        assertEquals('f', s.charAt(0));
        assertEquals('a', s.charAt(1));
        assertEquals('l', s.charAt(2));
        assertEquals('s', s.charAt(3));
        assertEquals('e', s.charAt(4));
    }
    
    @Test public void testSubstringOfEmptySubstring() {
        MyString s = MyString.valueOf(false).substring(1, 1).substring(0, 0);
        assertEquals(0, s.length());
    }
    ```
    
- 각 테스트는 일반적으로 객체를 만들고 수정한다.  그리고 각 작업과 유형을 감시하는 작업 또한 호출한다.
