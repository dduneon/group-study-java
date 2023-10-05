# Abstract Data Types

## Objectives

- **Abstract data types**
- **표현 독립성**

**이 단원에서 우리는 프로그램에서 데이터 구조를 사용하는 방법을 데이터 구조 자체의 특정 형식과 분리할 수 있게 해주는 `Abstract data type` 라는 강력한 아이디어를 살펴본다.**

**Abstract data type는 위험한 문제를 해결한다. 클라이언트는 type의 내부 표현에 대해 가정한다. 이것이 왜 위험한지, 어떻게 피할 수 있는지에 대해서 알아보겠다. 또한 작업 분류와 추상 데이터 유형에 대한 좋은 디자인의 몇가지 원칙에 대해서 논의할 것이다.**

---

## What Abstraction Means

Abstract data type는 소프트웨어 엔지니어링의 일반 원칙의 한 예이며 약간 다른 의미를 지닌 많은 이름으로 불린다. 

- **Abstraction(추상화)** : 더 단순하고 높은 수준의 아이디어로 낮은 수준의 세부 사항을 생략하거나 숨긴다.
- **Modularity(모듈성) :** 시스템을 구성 요소나 모듈로 나누는 것이다. 각 구성 요소는 시스템의 나머지 부분과 별도로 설계, 구현, 테스트, 추론 및 재사용할 수 있다.
- **Encapsulation(캡슐화) :** 모듈(하드쉘 또는 캡슐) 주위에 벽을 구축하여 모듈이 자체 내부 동작을 담당하고 시스템의 다른 모듈에 있는 버그가 모듈의 무결성을 손상시킬 수 없도록 한다.
- **information hiding(정보 은닉) :** 시스템의 나머지 부분에서 모듈 구현의 세부 사항을 숨기므로 시스템의 나머지 부분을 변경하지 않고도 해당 세부 사항을 나중에 변경할 수 있다.
- **Separation of concerns(관심사 분리):** 기능(또는 관심)을 여러 모듈에 분산 시키는 대신 단일 모듈의 책임으로 만든다.

소프트웨어 엔지니어로서 이러한 용어를 자주 접하게된다. 

---

## User-Defined Types

**컴퓨팅 초기에는 프로그래밍 언어에 내장된 유형(ex: 정수, 부울, 문자열 등)과 내장 프로시저(ex: 입력 및 출력용)이 함께 제공되었다. 사용자는 자신만의 절차를 정의할 수 있다.**

**→ 대규모 프로그램이 구축되는 방식**

**소프트웨어 개발의 주요 발전은 사용자 정의 유형도 허용하도록 프로그래밍 언어를 설계 할 수 있다는 `Abstract type` 이였다. 이 아이디어는 Dahl(Simula 언어의 창시자), Hoare(추상 유형을 추론하는데 사용하는 많은 기술을 개발한 사람), Parnas(정보 은닉이라는 용어를 처음으로 만든 사람)의 작업에서 나왔다.**

**데이터 추상화의 핵심 아이디어는 `type`이 `type`에 대해 수행할 수 있는 작업으로 특징지어진다는 것이다. 숫자는 더하고 곱할 수 있다. 문자열을 연결하여 `substring`을 가져올 수 있고 부울은 부정할 수 있다. 어떤 의미에서 사용자는 이미 초기 프로그래밍 언어에서부터 자신만의 type을 정의할 수 있었다.** 

ex) 일, 월, 연도에 대한 정수 필드를 사용하여 레코드 유형 날짜를 만들 수 있었다. 그러나 `Abstract type`을 새롭고 다르게 만든 것은 연산에 초점을 맞춘 것이다. 프로그래머가 컴파일러가 실제로 정수를 저장하는 방법을 무시할 수 있는 것과 마찬가지로 type의 사용자는 해당 값이 실제로 저장되는 방법에 대해서 걱정할 필요가 없다.

**많은 최신 프로그래밍 언어와 마찬가지로 Java에서는 `built-in types`과 `user-defined type`간의 구분이 모호하다. Integer 및 Boolean과 같은 java.lang 클래스가 내장되어 있다. Java.util의 모든 컬렉션을 내장된 것으로 간주하는지 여부는 덜 명확하다.(중요 X) 자바는 객체가 아닌 Primitive type을 사용하여 문제를 복잡하게 만든다.**

→ Int 및 boolean과 같은 이러한 유형 세트는 사용자가 확장할 수 없다.

`built-in type` : 자바에서 이미 만들어져 내장 되어 있음(int, double, boolean, char, String)

`user-defined type` : 기본 자료형 외에 프로그래머가 서로 관련된 변수들을 묶어서 하나의 타입으로 새로 추가하는 것(자바와 같은 객체지향언어에서는 클래스가 곧 user-defined type(사용자 정의)이다.

---

## ****Classifying Types and Operations****

built-in이거나 user-defined이든 관계없이 type은 `mutable` 또는 `immutable` 로 분류될 수 있다. `**mutable`객체는 변경될 수 있다. 즉, 실행될 때 동일한 객체에 대한 다른 작업의 결과가 다른 결과를 제공하는 작업을 제공한다.**  

**→ `Date` 객체는 `setMonth` 를 호출하고 `getMonth`작업으로 변경 사항을 관찰 할 수 있으므로 Date는 변경 가능하다.**

**→ 그러나 `String` 객체는 기존 객체를 변경하는 대신 새로운 `String` 객체를 생성하므로 변경 할 수 없는 `immutable` 객체이다.**

**때로는 type이 `mutable type` 과 `immutable type` 두가지로 제공된다.**

**ex)  `StringBuilder` 은 `String` 의 mutable version이다.(동일한 타입이 아니고 상호교환이 불가능)**

**abstract type의 작업유형을 이렇게 분류됨**

- **Creators(작성자)** : 해당 type의 새로운 개체를 만든다. 작성자는 객체를 인수로 사용할 수 있지만 생성되는 유형의 객체를 사용할 수 없다.
- **Producers(생산자)** : 해당 type의 이전 개체에서 새로운 개체를 만든다. 예를 들어 `String` 의 concat메소드는 두 개의 문자열을 가져와서 연결을 나타내는 새 문자열을 생성한다.
- **Observers(관찰자)** : abstract type의 객체를 취하고 다른 유형의 객체를 반환한다. 예를 들어 `List` 의 `size` 메서드는 `int`를 반환한다.
- **Mutators(중재자) :** 객체를 변경한다. 예를 들어 `List`의 `add` 메서드는 끝에 요소를 추가하여 목록을 변경한다.

**이러한 차이점을 다음과 같이 개략적으로 요약할 수 있다.**

- **creator: t* → T**
- **producer: T+, t* → T**
- **observer: T+, t* → t**
- **mutator: T+, t* → void|t|T**

**이는 다양한 클래스의 작업 시그니처의 모양을 보여준다.**

**각 T는 Abstract type자체이다. 각 t는 다른 type이다. + 표시는 해당 type이 시그니처의 해당 부분에서 한 번 이상 나타날 수 있음을 나타내고, * 표시는 해당 유형이 0번 이상 발생함을 나타낸다.** 

**ex) producer은 `String.concat()` 처럼 추상 유형의 두 값을 취 할 수 있다.**

**왼쪽에 있는 t의 발생도 생략될 수 있다. 일부 observer는 인수를 취하지 않고 일부는 여러 인수를 취하기 때문이다.**

**creator의 작업은 종종 `new ArrayList()` 와 같은 생성자로 구현된다. 하지만 단순히 `Arrays.asList()` 와 같은 정적 메서드로 구현될 수 있다.**

→ 정적 메서드로 구현된 구현 메서드를 종종 팩토리 메서드라고도 함

→ 자바의 다양한 `String.valueOf()` 메서드는 팩토리 메서드로 구현된 생성자의 다른 예이다.

**Mutator은 종종 `void` 리턴 타입으로 신호를 받는다. 어떤 종류의 `side-effect` 가 있는 경우 void를 반환하는 메서드를 호출해야한다. 그렇지 않으면 아무것도 반환하지 않기 때문이다.** 

**그러나 모든 mutator이 void를 반환하는 것은 아니다. 예를 들어 `Set.add()` 는 집합이 실제로 변경되었는지 여부를 나타내는 true/false를 반환한다. 자바 GUI 툴에있는 `Component.add()` 는 객체 자체를 반환하므로 여러 add()호출을 함께 연결 할 수 있다.**

### Abstract Data Type Examples

다음은 추상 데이터 유형의 몇 가지 예와 일부 작업을 종류별로 그룹화한 것이다.

`**int` 자바의 기본 정수 유형이다. 불변이므로 `mutator`이 없다.**

- Creators : 숫자 리터럴 0, 1, 2,…
- Producers : 산술 연산자 +, -, x, 1/4
- Observers : 비교 연산자 ==, ≠, < , >
- Mutators : 없음(불변)

`**List` 자바의 리스트 타입이다. `List`는 인터페이스이기도 하다.** 

**→ 이는 다른 클래스가 데이터 유형의 실제 구현을 제공한다는 것을 의미한다.ex) `ArrayList`, `LinkedList`**

- Creators : `ArrayList` 및 `LinkedList` 생성자, `Collections.singletonList`
- Producers : `Collections.unmodifiableList`
- Observers : `size` , `get`
- Mutators : `add`, `remove`, `addAll`, `Collection.sort`

`String` 자바의 문자열 유형이고 `immutable`이다.

- Creators : `String` 생성자
- Producers : `concat` , `substring` , `toUpperCase`
- Observers : `length` , `charAt`
- Mutators : 없음(불변)

**이 분류에는 몇 가지 유용한 용어를 제공하지만 완벽하지는 않다.** 

**예를 들어 복잡한 데이터 타입에는 Creator이자 Mutator인 작업이 있을 수 있다. 어떤 사람들은 Mutator이 없는 작업에만 Creator이라는 용어를 사용한다.**

---

## ****Designing an Abstract Type****

**Abstract Type을 디자인 하기 위해서는 올바른 작업을 선택하고 해당 작업이 어떻게 작동해야하는지 결정해야 한다.**

**복잡한 작업을 많이 하는 것 보다 강력한 방법으로 결합할 수 있는 몇가지 간단한 작업을 사용하는 것이 좋다.**

- **각 작업에는 잘 정의된 목적이 있어야 하며, 특별한 경우가 아닌 일관된 동작이 있어야 한다.**

ex) `list` 에 `sum` 메서드를 추가하면 안된다. 

→ 정수 목록으로 작업하는 클라이언트에게 도움이 될 수 있지만 문자열 목록을 어떻게 될까? `sum`메서드는 이러한 특별한 경우는 작업을 이해하고 사용하기 어렵게 만든다.

- **작업 세트는 클라이언트가 원하는 종류의 계싼을 수행하기에 충분해야 한다는 점에서 적절 해야 한다.**

**좋은 테스트는 해당 유형의 객체의 모든 속성을 추출할 수 있는지 확인하는 것이다.** 

ex) 가져오기 작업이 없으면 list의 요소가 무엇인지 알 수 없다. 기본 정보를 얻는 것이 지나치게 어려워서는 안된다.

→ 실패할 때까지 인덱스 증가에 대해서 get을 적용할 수 있기때문에 List에 대한 방법이 꼭 필요한 것은 아니지만 이는 비효율적이고 불편하다.

- **Abstract type는 List, Set, Graph등 일반적인 유형일 수 았다. 또는 distance map, employee database 등 특정 도메인일 수 있다.**

**→ 그러나 일반 기능과 도메인 별 기능을 혼합해서는 안됨**

ex) 카드 놀이를 나타내기 위한 `Deck Type`에는 정수나 문자열과 같은 임의의 개체를 허용하는 일반 `add` 메서드가 있어서는 안된다. 반대로,  `dealCards` 와 같은 도메인 메서드 별 메서드를 일반 타입 `List` 에 넣는 것은 의미가 없다.

---

## ****Representation Independence****

중요한 점은 좋은 Abstract data type은 representation independence(표현 독립적)이어야 한다는 것이다. 

**→ 이는 `Abstract type`의 사용이 해당 표현(실제 데이터 구조 또는 이를 구현하는 데 사용되는 데이터 필드)과 무관하므로 표현의 변경이 Abstract type 자체 외부의 코드에 영향을 미치지 않음을 의미한다.**

ex) `List`가 제공하는 작업은 List가 `linked list` 인지 `arrayList`인지는 중요하지 않다.

**클라이언트가 무엇에 의존해야 하는지, 무엇을 안전하게 변경할 수 있는지 알 수 있도록 해당 작업이 `preCondition`과 `postCondition`으로 완전히 지정되지 않는 한 ADT의 표현을 전혀 변경할 수 없다.**

### ex) 문자열에 대한 다양한 표현

`Representation Independence(표현 독립성)`이 무엇을 의미하고 왜 유용한지 알아보기 위해 간단한 Abstract data type을 살펴보자.

**예시인 `MyString` type은 실제 Java의 String보다 작업 수가 훨씬 적고 스펙도 약간 다르지만, 여전히 설명을 위해서다. ADT의 스펙은 다음과 같다.**

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

**이러한 pulbic operation과 스펙은 우리가 알 수 있는 유일한 정보이다.**

- **실제로 테스트 우선 프로그래밍 패러다임에 따라서 우리가 만들어야 하는 첫 번째 클라이언트는 해당 스펙에 따라서 이러한 작업을 실행하는 `test suite`이다.**
- **그러나 현재로서 `MyString` 객체에 직접 AssertEquals를 사용하는 테스트 케이스를 작성하는 것은 작동하지 않는다. → `MyString`에 `equals` 연산을 만들어놓지 않았기 때문**

→ 현재 `MyString`로 할 수 있는 유일한 작업은 `valueOf` , `length`, `charAt` , `substring` 뿐이다.

**우리의 테스트는 이 작업들로 제한되어야 한다.  valueOf 작업에 대한 테스트**

```java
MyString s = MyString.valueOf(true);
assertEquals(4, s.length());
assertEquals('t', s.charAt(0));
assertEquals('r', s.charAt(1));
assertEquals('u', s.charAt(2));
assertEquals('e', s.charAt(3));
```

`**MyString`에 대한 간단한 표현을 살펴보겠다. 문자 배열, 정확히 문자열 길이, 끝에 추가 공간이 없는 것이다.**

`**internal representation` 을 클래스 내의 인스턴스 변수 선언하는 방법 다음과 같습니다.**

```java
private char[] a;
```

**이러한 표현 선택을 통해 작업은 간단한 방식으로 구현된다.**

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

생각 해 볼 질문 :

- `**charAt` 과 `substring` 이 해당 파라미터가 유효한 범위내에 있는지 확인해야 하는 이유는 무엇인가?**
- **클라이언트가 이러한 잘못된 입력으로 이러한 구현을 호출하면 어떻게 될 것이라고 생각하는가?**

이 구현의 한 가지 문제점은 성능 개선의 기회를 놓치고 있다는 것이다. 

- **이 데이터 타입은 `immutable`이기 때문에 `substring` 작업에서 새로운 배열로 문자들을 복사 할 필요가 없다.**
- **기존 `MyString`객체는 자신의 문자 배열을 가르키고 새 하위 문자열이 나타내는 시작과 끝을 추적 할 수 있다. → 일부 Java 버전의 문자열 구현은 이를 수행한다.**

이 최적화를 구현하기 위해서 이 클래스의 내부 표현을 다음과 같이 변경할 수 있다.

```java
private char[] a;
private int start;
private int end;
```

이렇게 표현을 한다면 이 클래스의 operation은 다음과 같이 구현된다.

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

`**MyString`의 기존 클라이언트는 private filed가 아닌 public method의 스펙에만 의존하기 때문에 모든 클라이언트 코드를 검사하고 변경할 필요 없이 이러한 변경을 수행할 수 있다.**

**→ `repersentation independence` 의 효과**

---

## ****Realizing ADT Concepts in Java****

모든 언어의 프로그래밍에 일반적으로 적용할 수 있는 이 문서에서 논의한 몇가지 일반적인 아이디어와 Java언어 기능을 사용한 특정 구현을 요약해보겠다.

**이 단원에서 아직 논의되지 않은 표의 항목 부분은 Creator(생성자) 작업으로 상수 객체를 사용한다.
이 패턴은 Immutable에서 흔히 볼 수 있따. 여기서 유형의 가장 단순하거나 가장 비어있는 값은 단순히 public constant이고 procuders는 좀 더 복잡한 값을 구축하는 데 사용된다.**

| ADT concept | Ways to do it in Java | Examples |
| --- | --- | --- |
| Creator operation | Constructor
Static(factory)
method
Constant | ArrayList()
Collections.singletonList()
Arrays.toList()
BigInteger.ZERO |
| Observer
operation | Instance method

Static method | List.get()

Collections.max() |
| Producer
operation | Instance method

Static method | String.trim()

Collections.unmodifiableList() |
| Mutator operation | Instance method

Static method | List.add()

Collections.copy() |
| Representation | private fields |  |

---

## ****Testing an Abstract Data Type****

**각 작업에 대한 테스트를 생성하여 추상 데이터 유형에 대한 test suite를 구축한다. 이러한 테스트는 필역적으로 서로 상호 작용한다. `creators`, `producers` , `mutators` 를 테스트하는 유일한 방법은 결과 개체에 대해 `observers` 를 호출하는 것이다.**

`**MyString` 유형에서 네 가지 작업의 입력 공간을 분할하는 방법이다.**

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

그런 다음 이러한 모든 파티션을 포괄하는 테스트 케이스는 다음과 같다.

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

각 테스트 케이스는 일반적으로 해당 유형의 객체(Creators, producers, mutators)를 만들거나 수정하는 몇가지 작업과 해당 유형의 객체(observers)를 검사하는 일부 작업을 호출한다. 결과적으로 각 테스트 케이스는 여러 작업의 일부를 다룬다.

---

## Summary

- abstract data type는 작업으로 특징 지어진다.
- operation(작업)은 creators, producers, observers, mutators로 분류 할 수 있다.
- ADT의 스펙은 operation의 집합과 해당 스펙이다.
- 좋은 ADT는 단순하고 일관적이면서 적절하고 표현에 독립적이다.
- ADT는 각 작업에 대한 테스트를 생상하여 테스트하지만 동일한 테스트에서 Creators, Producers, Observers, Mutators를 함께 사용한다.

**SFB** : 좋은 ADT는 데이터 유형에 대해 잘 정의된 계약을 제공하므로 클라이언트는 데이터 타입에서 무엇을 기대할 수 있는지 알 수 있고 구현자는 자유롭게 변경할 수 있다.

**ETU :** 좋은 ADT는 간단한 작업 뒤에 구현을 숨기므로 ADT를 사용하는 프로그래머는 구현 세부 사항이 아닌 작업만 이해하면 된다.

**RFC :** 표현 독립성을 통해 클라이언트의 변경을 요구하지 않고도 abstract data type를 변경할 수 있다.