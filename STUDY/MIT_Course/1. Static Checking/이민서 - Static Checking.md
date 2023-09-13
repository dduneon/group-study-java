# 이민서 - Static Checking

참조 사이트 [http://web.mit.edu/6.005/www/fa15/classes/01-static-checking/](http://web.mit.edu/6.005/www/fa15/classes/01-static-checking/)

- static typing

---

- the big three properties of good software

---

# Hailstone Sequence

```java
2, 1
3, 10, 5, 16, 8, 4, 2, 1
4, 2, 1
2n, 2n-1 , ... , 4, 2, 1
5, 16, 8, 4, 2, 1
7, 22, 11, 34, 17, 52, 6, 13, 40, ...? (where does this stop?)

```

> n이 짝수이면 n/2, n이 홀수이면 3n+1
시퀀스는 1에 도달하면 종료
> 

```java
int n = 3;
while (n != 1) {
    System.out.println(n);
    if (n % 2 == 0) {
        n = n / 2;
    } else {
        n = 3 * n + 1;
    }
}
System.out.println(n);

```

- 명령문 끝 세미콜론
- 가독성을 위해 명령문 여러 줄
- if,while 조건 주위 괄호
- 블록 들여쓰기 , 나중에 보기 편하기 위해

> Index
#basic_semantics 기본적인 의미론
> 

---

# Type

> Primitive Types (소문자)
> 
> 
> > int
> long
> boolean
> double
> char
> > 

> object types (대문자)
> 
> 
> > String
> BigInteger
> > 
- 자바는 관례상 primitive type은 소문자 object type은 대문자로 시작합니다.

---

# Static Typing

- Java is a **statically-typed language**
- 자바는 **정적 유형** 언어 입니다.
- Static Typing은 특정한 static checking검사로 컴파일 시간에 버그를 검사하는 것을 의미
- 이 과정에서 버그를 제거 하는것이 목표

---

# Static Checking, Dynamic Checking, No Checking

- **자바가 제공하는 세가지 종류의 자동 검사**_
1. ***Static Checking*** (정적 검사)
    - 프로그램이 실행되기 전 버그가 자동으로 발견
        - 오타 , 구문 오류
        - `Math.sine(2)`과 같은 오타 (올바른 이름 `sin`)
        - 인수 개수 `Math.sin(30,20)`
        - 잘못된 인수 유형 `Math.sin("30")`
        - `return "30";` 잘못된 return type 유형 예) `int`
2. ***Dynamic Checking*** (동적 검사)
    - 코드가 실행될 때 버그가 자동으로 발견
        - 잘못된 인수 값
        - `x/y` 경우 `y` 가 0인 정수 표현식
        - 표현할 수 없는 반환 값
        - 범위를 벗어난 인덱스
        - null 객체 참조에 대한 메소드 호출
3. ***No Checking*** (확인 안함)
    - 오류 찾는데 전혀 도움되지 않는다.

---

# Surprise - Primitive Types Are Not True Numbers

- Primitive type은 진정한 숫자가 아닙니다.

Primitive Type이 정수 및 실수 처럼 작동하지 않는 코너 케이스가 있다

- Integer division. `5/2`분수를 반환하지 않고 잘린 정수를 반한한다 (분수는 정수로 표현할 수 없다)
- Integer overflow. `int`및 유형 `long`은 실제로 최대값과 최소값을 갖는 유한한 정수 집합
    - 범위를 넘어서는 경우 오버플로
- Special values in `float`and `doubles`. 0으로 나누거나 음수의 제곱근을 취하는 등 동적 오류가 발생할 것으로 예상되는 연산은 이러한 특수 값중 하나 생성
계속 계산을 하다 보면 최종적으로 안 좋은 답을 얻게 된다
`double``NaN``POSITIVE_INFINITY``NEGATIVE_INFINITY`

---

# Arrays and Collections

배열 크기 할당
<pre><code>int[] a = new int[100];</code></pre>

- 배열 `int[]`유형에는 가능한 모든 배열 값이 포함
- 특정 배열은 생성되면 길이 변경 불가
- `length` `a.length`는 `String.length()`과 다릅니다 `a.length`는 메서드 호출이 아니므로 뒤에 괄호를 넣지 않습니다
- 배열의 고정 길이 배열은 오버플로와 같은 버그에 위험

---

가변 길이 시퀀스  **[[ArrayList]]**

```
  List<Integer> list = new ArrayList<Integer>();

```

- indexing.list.get(2)
- assignment list.set(2,0)
- length list.size()

> List는 new로 직접 생성할 수 없지만 대신 List가 제공하는 인터페이스 타입이 있다
ADT는 나중에...
ArrayList is a class  해당 작업의 구현을 제공하는 구체적인 유형
ArrayList는  List Type의 유일한 구현은 아니지만 일반적으로 사용되는 Type
자세한건 Java API(Application Programmer Interface ,또는 Library)
> 

> List<int> 대신 List<Integer>를 사용
Primitive type이 아닌 Reference type 사용
> 

ArrayList를 사용한 Hailstone code

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

Not only simpler but safer too 목록은 추가하는 숫자에 맞춰 자동으로 커지기 때문
(메모리가 부족해질때까지)

---

# Iterating

```java
int max = 0;
for (int x : list) {
    max = Math.max(x, max);
}

```

list는 물론 배열까지 반복할 수 있습니다.

---

# Methods

Java에서 명령문은 일반적으로 메소드 내부에 있어야 하며 모든 메소드는 클래스에 있어야함

Hailstone 프로그램을 작성하는 가장 간단한 방법

```java
public class Hailstone {
      /**
       * Compute a hailstone sequence.
       * @param n  Starting number for sequence.  Assumes n > 0.
       * @return hailstone sequence starting with n and ending with 1.
         */
    public static List<Integer> hailstoneSequence(int n) {
      List<Integer> list = new ArrayList<Integer>();
      while (n != 1) {
          list.add(n);
          if (n % 2 == 0) {
              n = n / 2;
          } else {
              n = 3 * n + 1;
          }
      }
      list.add(n);
      return list;
    }
  }

```

- `public`은 프로그램의 모든 코드가 클래스나 메서드를 참조할수 있음을 의미
- `private`은 프로그램 안정성 높이고 불변성 보장
- `static`은 메소드가 self매개변수를 취하지 않는다는 것을 의미합니다.

정적 메서드를 호출하는 올바른 방법은 클래스이름으로 호출

```
Hailstone.hailstoneSequence(83)

```

---

# Mutating Values vs Reassigning Varables

- 값 변경과 변수 재할당

배열이나 List와 같은 변경 가능한 값의 내용에 할당하면 값 내부의 참조가 변경

좋은 프로그래머는 예기치 않게 바뀔수 있는것을 대비해 불변성을 사용

Immutable types(불변 유형) 생성되면 값이 절대 변경 될수 없는 유형

- final 키워드 사용

```
  final int n = 5;

```

---

# Documenting Assumtions

프로그램 두가지 목표를 염두에 두고 작성

1. 컴퓨터와 통신 합니다. 먼저 프로그램이 구문적으로 정확하고 type이 정확하다는 점을 컴파일러를 통해 확인 그다음 런타임에 올바른 결과를 제공하도록 논리를 올바르게 설정
2. 다른 사람들과 의사소통 하기, 프로그램을 이해하기 쉽게 만들기 나중에 누군가 수정을 할수 있기 때문이다.

---

# Hacking vs Engineering

코드 나쁜 점

- 테스트 하기전에 많은 코드를 작성하는 것
- 모든 세부 사항을 코드에 기록하는 대신 영원히 기억할 것이라고 가정하는 것
- 버그가 존재하지 않거나 찾기 쉽고 수정하기 쉽다고 가정하는 것

코드 좋은 점

- 한 번에 조금씩 작성하면서 테스트
- 코드가 의존하는 가정을 문서화
- 정적 검사

---