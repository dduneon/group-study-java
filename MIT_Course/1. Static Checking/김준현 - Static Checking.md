# 김준현 - Static Checking

# Hailstone Sequence

> Hailstone in korean? 우박

---

- Next Number in the sequence ?
  - Even Number: `n/2`
  - Odd Number: `3n+1`
  - The Sequence ends when it reaches 1

```jsx
2, 1
3, 10, 5, 16, 8, 4, 2, 1
4, 2, 1
2n, 2n-1 , ... , 4, 2, 1
5, 16, 8, 4, 2, 1
7, 22, 11, 34, 17, 52, 6, 13, 40, ...? (where does this stop?)
```

- It’s conjectured that all hailstones eventually fall to the ground
- Why is it called a hailstone sequence?
  - Hailstones form in clouds by bouncing up and down, until they eventually build enough weight to fall to earth

# Computing Hailstone

---

```java
int n = 3;
while(n != 1) {
	System.out.println(n);
	if(n % 2 == 0)
		n /= 2;
	else
		n = n * 3 + 1;
}
```

```python
n = 3
while n != 1:
    print n
    if n % 2 == 0:
        n = n / 2
    else:
        n = 3 * n + 1

print n
```

- Basic Semantics of Expression and Statements?
  - Python과 매우 유사
- Extra punctuation (\*Seperators: semicolons, qustion marks, pairs of parenthesis …)
  - 코드를 `organize` 하는 방법에 대한 자유도
  - can split a statement into multiple lines → more Readability
- Curly braces around blocks, instead of indentation (중괄호)

# Types

---

> Type? Set of values, along with operations that can be performed on those values

> 연산자와 함께 붙어서 그들의 값을 나타낼 수 있는 집합

## Primitive Types

- `int`
  - Range : +- 2^31
- `long`
  - Range : +- 2^63
- `boolean`
- `double`
  - 실수(real number)의 하위 집합을 나타낼 수 있음
- `char`

- `float, byte, short`

## Object Types

- `String`
- `BigInteger`

- integer of arbitrary size

> primitive types → lowercase, object types → capital letter

### Operations ?

입력을 받아서 출력을 제공하는 함수

```java
a + b
// + : int x int -> int

bigint1.add(bigint2)
// add: BigInteger x BigInteger -> BigInteger

Math.sin(theta)
// sin: double -> double
	// Math ? Not an Object, Its' the class that contains the sin function
```

- 몇몇의 `operations` 는 같은 연산자 이름이 다른 types에 사용되는 것을 위하여 `overloaded` 된다
- Arithmetic Operations (+, -, \*, /) 들 또한 `numeric primitive types` 들에 지원되기 위하여 heavily overloaded 된다

# Static Typing

---

> Java 는 Statically-typed language 이다

> Compile time 에 모든 변수의 Types를 알 수 있다 (Before the program runs)

> 또한 컴파일러가 모든 Expression들의 Type을 추측할 수 있음

```java
int a = 2;
int b = 3;
??? c = a + b;
// c의 Type은 Statically-typed(Compile time에 Type Checking) 된다
```

- Dynamically-Typed language → Python

### Static Typing은 `Static Checking` 의 특정 종류이다

- Compile time에 bug를 checking 한다는 뜻
- 코드에서 버그를 제거하는 첫번째 아이디어이다
- 올바르지 않은 타입의 arguments를 operation에 적용했을때 야기되는 오류를 방지

# Static Checking, Dynamic Checking, No Checking

---

- **Static checking**: 프로그램이 실행되기 전에 버그를 자동적으로 찾는다
- **Dynamic checking**: 코드가 실행될 때 자동적으로 찾는다
- **No checking**: 버그를 찾는데에 도와주지 않는다

## Static Checking

- Syntax Errors (extra punctuation or spurious word)

## Dynamic Checking

- Illegal Argument Values
  - divide by `zero`
- Unrepresentable return value
  - specific return value can’t be represented in the java
- Out-of-range index
- Calling a method on a `null` object reference

- **Static Checking**은, 변수가 가지는 특정 값과 독립적인 타입이나 에러에 관한 것인 경향이 있다.
- Dynamic Checking은, 대조적으로 특정 값에 의해 발생하는 에러에 관한 것

# Surprise: Primitive Types Are Not True Numbers

---

`Primitive numeric type` 들은 예를 들어 우리가 실제로 사용했던 수나, `Integer`과 같이 행동하지 않을 수 있는 `Corner case`들을 가지고 있음,

> Edge case: 알고리즘이 처리하는 데이터의 값이 알고리즘의 특성에 따른 일정한 범위가 넘을 경우 발생하는 문제

> Corner case: 여러가지 변수와 환경의 복합적인 상호작용으로 발생하는 문제

- **Integer division**: `5/2` 는 분수를 반환하지 않고 잘린 정수를 반환한다
  - 이는 분수가 정수로 표현될 수 없으므로, Dynamic Error 가 되기를 바랬지만 그러지 않아 오답을 자주 발생시킨다
- Integer Overflow: `int` 나 `long` 은 최소, 최대 값이 존재하는 정수의 유한적인 집합이다
  - too positive 하거나, too negative 하다면? Overflow 발생하고 legal range이지만 올바르지 않은 답 발생
- Special values in `float` and `doubles` : `float` 와 `double` 의 경우 실제 숫자가 아니지만, 특별한 값을 갖을 수 있음
  - `NaN`(Not a Number), `POSITIVE_INFINITY`, `NEGATIVE_INFINITY`
  - Dynamic Error을 발생시킬 것이라 예측했던 dividing by zero나 negative number square root에서 특별한 값을 대신 발생한다

# Arrays and Collections

---

> Hailstone Computation의 Sequence를 Data structure에 저장하도록 한다면?

## Java에서 두가지의 방식, Arrays와 Lists

### Array

> another type `T`에 대한 고정 길이의 sequence

- 한번 생성되면 배열의 크기를 변경할 수 없다

```jsx
//Indexing
a[2];

//Assignment
a[2] = 0;

//Length
a.length;
//different syntax from String.length()
//a.length != method call
```

```java
int[] a = new int[100];  // <==== DANGER WILL ROBINSON
int i = 0;
int n = 3;
while (n != 1) {
    a[i] = n;
    i++;  // very common shorthand for i=i+1
    if (n % 2 == 0) {
        n = n / 2;
    } else {
        n = 3 * n + 1;
    }
}
a[i] = n;
i++;
```

위와 같이 Array를 생성하고, index를 저장하는 변수인 i를 사용하여 sequence의 값을 저장

> 하지만, 이 경우 100개의 int 배열의 범위를 넘어가는 반복을 하게 된다면? 버그가 발생할 것이다
> C, C++의 경우 Array Index의 runtime checking(dynamic checking)을 수행하지 않기 때문에 많은 문제를 야기시켰다

### List

> `T`에 대한 가변 길이의 sequence

```java
//Indexing
list.get(2)

//Assignment
list.set(2, 0)

//Length
list.size()
```

- `List`는 인터페이스이다
  - 따라서, 타입을 `new` 키워드로 인스턴스를 직접 생성할 수 없다
  - 대신, `List` 가 반드시 제공해야하는 작업들을 지정
  - 이는 `Abstraction Data Types` … ?
- `ArrayList` 는 클래스이다
  - operation들을 구현하는 구체적인 유형의 클래스
  - `List` 인터페이스의 유일한 구현은 아니다, 하지만 `LinkedList` 는 다르다

![Untitled](./image/jh-1.png)

- `List<Integer>` 을 사용하여야 한다
  - `List<int>` 는 사용할 수 없음
  - `List` 는 `primitive type` 을 다루지 않는다
  - `Object Type` 이 Angle brackets `()` 를 가지고 parameterize할 때, Object type equivalents를 사용하도록 요구한다
    - Equivalents Object type? `int` ↔ `Integer`
  - 이러한 요구사항의 유일한 이유는 primitive value보다 더 많은 메모리를 사용하는 object가 실제로 포함되어 있다는 것을 알려주기 위해서이다
  - - 하지만? `int` 와 `Integer` 의 경우 아니다?!
  - Primitive ? Object ?
    For efficiency. Variables of primitive types contain the value directly; variables of non-primitive types are references, referring to an object stored somewhere else in memory.
    Each time you need to use the value of a wrapper type, the JVM needs to lookup the object in memory to get at the value. This isn't needed for primitive types, because the variable contains the value itself, instead of a reference to an object that contains the value.
    However, that doesn't explain why primitive types need to be explicitly visible in the Java programming language. The designers of the Java language and the JVM could have chosen to hide primitive types from the language itself, so that you could treat everything as an object; the compiler could then translate it under the covers to more efficient primitive types.
    Some newer programming languages that run on the JVM (Groovy, Scala and others) let you do exactly that: in the language itself everything looks like an object, which you can for example call methods on, but below the covers the compiler translates them to primitives.
    I guess that in the time the Java language was developed (in the first half of the 1990's) people didn't think of that, and now it's too late for a radical change in the language to allow this.
- 단순하고, 더 안전한 방법이다

```java
List<Integer> list = new ArrayList<Integer>();
int n = 3;
while (n != 1) {
    list.add(n);
    if (n % 2 == 0) {
        n = n / 2;
    } else {
        n = 3 * n + 1;
    }
}
list.add(n);
```

# Method

---

> Java에서 **statement**는 일반적으로 method안에 있어야 하고, 모든 method는 class 안에 있어야 한다

- statement/expression

  ## Statement ?

  프로그램 안에서 하나의 동작을 기술하는 것, 블록(`{}`) 안에 모여서 method와 class를 구성함
  statement의 끝에는 항상 세미콜론을 붙여 준다
  실행 가능한(executable) 최소의 독립적인 코드 조각
  컴파일러가 이해하고, 실행할 수 있는 모든 구문은 statement
  return 3; → Statement, not expression
  / ?

  ## Expression?

  값을 기술하는 것. 즉, 식은 하나의 값으로 치환된다.

  ```java
  j = (i = 5);
  // i=5 뒤에는 세미콜론이 붙지 않았다 -> 이는 j에 할당되는 값으로 환원되는 식으로 사용됨
  // 따라서 이처럼 두가지 expression으로 이루어진 statement

  i = 5;
  // 이는 하나의 expression으로 이루어진 statement
  // = expression statement

  i++;
  i = 100;
  System.out.println("test");

  method(p1, p2);
  // 함수 호출을 제외한 expression은 value와 operation으로 구성된다

  break;
  // this is statement, 값으로 환원될 수 없음
  // ?
  ```

  - null value
  - variable access(Object.value)
  - method invocation(메서드 호출)
  - Object creation
  - instanceof
  - Anonymous Class

### Public

프로그램에 어느 곳에서든 모든 코드가 class 또는 method를 참조할 수 있음을 의미

- 반대로 `private` 는 프로그램에서 더 큰 안전성을 얻기 위하여 사용한다
  - immutable type들에게 immutability 를 제공한다
  - Immutable type
    ## Immutable
    생성 후 그 상태를 바꿀 수 없는 객체(object)
    객체를 복제할 때 객체 전체가 아니라 단순히 참조만 복사하고 끝난다
    멀티 스레드 프로그래밍에서 유용하다(mutual exclusion)을 할 필요가 없다, 복수의 스레드에 의해 특정 스레드의 변경될 우려가 없기 때문이다

# Mutating Value vs. Reassignment Variables

---

> `Array`, `List` 에서 Mutable Value의 내용에 할당할 때, 해당 값 내부의 참조를 변경

### Immutability (immunity from change)

- major design principle
- 만들어진 후 값이 절대 변하지 않는다
- Java에서는 `final` 키워드를 사용한다
- 이는 static checking을 하여 runtime에 오직 한번만 할당되도록 한다
- 읽는 사람이 더 효율적으로 읽을 수 있고, 컴파일러가 statically check 하도록 한다

# Documenting Assumptions

---

> 변수의 type에 관한 가정을 적는 것

ex) 이 변수는 Integer을 나타낼 것이다

- Java는 Compile time에 이러한 가정을 체크하고, 프로그램에서 이 가정을 위반하는 곳이 없는 것을 보장한다
- 변수를 `final` 로 선언하는 것 또한 documentation의 한 형태이다
  - 이는 해당 변수가 초기값을 할당한 이후에 절대 변하지 않을 것이라는 것을 의미한다
  - 이는 자바가 Statically check 한다
- 하지만 `n`이 반드시 양수이다 라는 것을 자동으로 체크해주지는 않는다

### Why do we need to write down our assumption?

- 만일 우리가 그것들을 적어주지 않고 잊어버리게 되면, 우리의 프로그램을 읽거나 바꿔야 하는 다른 사람들이 그것들을 알지 못한다.

### 프로그램은 두 가지 목표를 가지고 쓰여야 한다

- Communicating with the computer
  - 컴파일러에게 내 프로그램이 문법적으로 올바른지, type이 올바른지 이해시켜라!
    - `final` keyword를 사용하면? 우리가 해당 변수를 reassignment 하려고 하면 statically check 할 수 있음
- Communicating with other people
  - 프로그램을 이해하기 쉽게 작성하라
    - 프로그램을 고치려고 할 때, 향상시키고자 할 때, 새로운 기술에 적용시키고자 할 때

# Hacking vs. Engineering

---

- 한번에 조금씩 프로그램을 작성하고, 테스트한다
  - Test-First Programming
- 코드에 의존하는 Assumption들을 문서화 한다

# Reference

---

[https://wisdom-and-record.tistory.com/6](https://wisdom-and-record.tistory.com/65)5

[https://shoark7.github.io/programming/knowledge/expression-vs-statement](https://shoark7.github.io/programming/knowledge/expression-vs-statement)

[https://docs.oracle.com/javase/8/docs/api/java/util/List.html](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)

[https://web.mit.edu/6.005/www/fa15/classes/01-static-checking/](https://web.mit.edu/6.005/www/fa15/classes/01-static-checking/)
