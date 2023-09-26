# Avoiding Debugging

**목표:**

**디버깅을  완전히 방지하거나 디버깅을 해야 할 때 쉽게 사용할 수 있는 방법을 배운다.**

---

## First Defense: Make Bugs Impossible

버그를 위한 최고의 방어는 설계상 버그를 불가능하게 만드는 것이다.

- **우리는 첫번째 단원에서 `static checking` 에 대해서 이야기 한 적있다. `static checking` 는 컴파일 타임에 버그를 잡아서 많은 버그를 제거해준다.**
- **또한 `dynamic checking` 를 이야기 한 적있다. 예를 들어 자바는 배열 오버플로우를 동적으로 잡아내서 이를 불가능하게 만든다. 만약 `array` 또는`List` 의 경계 이상에 인덱스로 접근하려고 할 때 자바는 자동으로 오류를 읽으킨다.(C, C++ 같이 오래된 언어는 오류 접근을 허용하기 때문에, 버그와 보안 취약점으로 이어짐)**

- `**Immutability` 는 버그를 방지하는 다른 디자인 원칙이다. `immutable type`는 값이 생성되면 절대 변경 할 수 없는 유형이다.**
- `**String` 은 `immutable`이다. String은 문자의 순서를 변경하는 메서드는 존재하지 않는다. 문자열은 수정 될 것이라는 걱정 없이 값을 전달하고 공유 할 수 있다.**
- **자바는 또한 `final` 키워드를 통해서 `immutable reference` 를 제공한다.  final 키워드가 붙은 변수에는 값이 한 번 할당 된 뒤 다시 할 당 할 수없다.**

→ 메소드 파라미터와 지역변수에 가능한 `final` 키워드를 많이 사용하는 것이 좋다. 변수 유형과 마찬가지로 이러한 선언은 중요한 문서가 되며, 컴파일러에 의해서 `static check`가 된다.

ex)

```java
final char[] vowels = new char[] { 'a', 'e', 'i', 'o', 'u' };

```

`**vowels` 변수는 final로 선언되었는데 정말 변하지 않는걸까?** 

**여기서 가능한 것은 무엇이고 잘못된 것은 무엇일까?**

```java
vowels = new char[] { 'x', 'y', 'z' };  // X
vowels[0] = 'z'; // O
```

---

## Second Defense: Localize Bugs

- **만약 버그를 방지 할 수 없을 때, 버그의 원인을 찾기 위해 전체를 볼 필요 없이 프로그램의 작은 부분에서 지역화하면 편하다.**
- **작은 메서드나 작은 모듈로 지역화 한다면 프로그램 텍스트를 보는 찾아보는 것만으로도 버그를 찾을 수 있다.**

**전에 이야기 했던 `Fail fast` (버그 발생 원인가 가까울 수록)를 이용하면 빠르게 해결 가능하다.**

ex)

```java
/**
 * @param x  requires x >= 0
 * @return approximation to square root of x
 */
public double sqrt(double x) { ... }
```

**만약 `sqrt` 메서드에 인자로 음수를 넘긴다면 어떻게 동작을 할까?**

- **호출자가 `x` 는 음수가 아니여야 한다는 요구사항을 충족하지 못했기 때문에 sqrt는 계약조건에 구속되지 않으므로 임의의 값을 반환하거나 무한루프에 빠지거나 CPU를 다운시키는 등 무엇이든 자유롭게 일어난다.**
- **그러나 잘못된 호출은 호출자의 버그를 나타내므로 가장 유용한 동작은 버그를 가능한 빨리 지적하는 것이다.**

**→ precondition을 테스트 하는 `runtime assertion` 을 삽입하여 이를 수행 할 수 있음**

```java
/**
 * @param x  requires x >= 0
 * @return approximation to square root of x
 */
public double sqrt(double x) { 
    if (! (x >= 0)) throw new AssertionError();
    ...
}
```

**precondition이 만족되지 않으면 이 코드는 AssertionError Exception를 던져 프로그램을 종료한다. 호출자의 버그 영향이 퍼지는 것을 방지한다.**

- **precondition을 확인하는 것은 `defensive programming` 의 한 예시이다.**
- **실제 프로그램에는 버그가 없는 경우가 거의 없다.**
- `**Defensive programming` 은 버그가 어디에 있는지 모르더라도 버그의 영향을 완화할 수 있는 방법을 제공한다.**

`**Defensive programming` : 예상치 못한 입력에도 한 소프트웨어가 계속적으로 기능을 수행 할 수 있도록하는 설계의 한 형태.**

---

## Assertions

**이러한 종류의 `defensive check` 를 위한 프로시저를 정의하는 것이 일반적인 관행이다. `assert` 라고 함**

```java
assert (x >= 0);
```

**이러한 접근 방법은 `assertion` 이 실패 할 때 무슨 일이 일어나는지 정확하게 추상화한다.**

**실패한 `assertion` 을 종료 될 수 있고 로그 파일에 기록 될 수 있고 관리자에게 이메일로 보고 될 수도 있다.**

- **assertion은 오류가 발생한 시점에 프로그램 상태에대한 가정을 문서화하는 추가 이점도 있다.**

**→ 코드를 읽는 누군가에게 `assert(x >= 0)` 는 이 지점에서 x 는 0보다 항상 크거나 같아야 한다고 말하는 것이다.**

- **주석과 달리 assertion은 런타임에 가정을 적용하는 실행가능한 코드이다.**

**자바에서 runtime assertions은 언어에 내장된 기능이다. assert문의 가장 간단한 형식은 boolean 형식을 사**

**용하고 그 값이 false로 나오면 AssertionError을 던진다.**

```java
assert x >= 0;
```

**Assert문에는 일반적으로 문자열로 이루어진 description expression이 포함 될 수 있지만 `primitive` 타입 혹은 object에 대한`reference`일 수 있다.**

**→ assertion이 실패한다면 오류 메시지가에 설명이 출력되므로 프로그래머에게 실패 원인에 대한 추가 정보가 제공된다.**

**ex) 설명은 콜론(:)으로 구분된 assert문 뒤에 나온다** 

```java
assert (x >= 0) : "x is " + x;
```

**만약 `x` 가 -1일 때 에러 메시지는 이렇게 발생한다.**

> **x is -1**
> 

**→ 코드에서 assert 문이 발견된 위치와 프로그램을 호출한 순서가 있는 `stack trace`와 함께 제공됨. 이러한 정보는 버그를 찾기 충분하다.**

**자바에서는 assertion옵션은 기본적으로 꺼져있다. (Assert를 확인하는것이 성능에 비용이 많이 들 수 있기 때**

**문에 그렇게 했다.) → 그러나 대부분의 Assertion은 코드에 비해서 비싸지 않으며 버그를 검사하는데서 얻은 이점은 성능면에서 비용을 들일 가치가 있다.**

**Junit 테스트 할 때는 assertion을 항상 활성화 하는 것이 좋다.**

**어설션이 활성화 되었는지 확인하는 방법**

```java
@Test(expected=AssertionError.class)
public void testAssertionsEnabled() {
    assert false;
}
```

**만약 assertion이 활성화 되어 있다면 assert문에서 false를 보고 `AssertionError`을 발생시킨다.**

**`annotation(excepted=AssertionError.class)`는 테스트의 오류 발생을 예상해서 테스트가 통과됨**

**그러나, assertion이 꺼져있다면 이 AssertionError이 발생하지 않으므로 Junit에서는 이 테스트가 실패한 것으로 간주한다.**

- **자바의 `assert` 는 Junit의 메서드 `assertTrue()` , `assertEquals()` 등과는 다르다.**

→ **모두 코드에 대한 `predicate` 를 주장하지만 다른 context에서 사용하기 위해 설계되었다.**

- **구현내부에 defensive checks를 위해서 구현 코드에서 assert 문을 사용해야한다.**

**Junit테스트 결과를 확인하기 위해서는 Junit에 assert…() 메서드를 사용해야한다.**

**→ `assert` 는 `-ea` 옵션을 설정 안해주면 실행 안하지만 Junit의 `assert...()` 는 바로 사용가능하다.**

---

## What to Assert

assert를 위한 몇가지 사항

`sqrt` 에서 본 것 같은 **Method argument requirement (메서드 인자 요구사항)**

**Method return value requirements(메서드 반환 값 요구 사항) : 이런 종류의 주장을 `self check`라고 한다.**

**ex) sqrt 메소드는 결과를 제곱하여 x에 합리적으로 가까운지 확인 할 수 있음**

```java
public double sqrt(double x) {
    assert x >= 0;
    double r;
    ... // compute result r
    assert Math.abs(r*r - x) < .0001;
    return r;
}
```

**Covering all cases(모든 사건을 다루고 있음). 조건문이나 switch문에서 모든 사례를 포함하지 않는 경우, assert를 사용하여 잘못된 케이스를 차단하는 것이 좋다.**

```java
switch (vowel) {
  case 'a':
  case 'e':
  case 'i':
  case 'o':
  case 'u': return "A";
  default: Assert.fail();
}
```

**assertion은  `vowel` 이 5개의 모음 문자 중 하나여야 한다는 효과를 가진다.**

**assertion은 언제 작성해야 할까? - 코드를 작성 할 때 적어야 한다.(코드를 작성할 때 `invariant`를 염두에 두고 작성해야 한다.)**

→ assertion 작성을 미루면 중요한 Invariant를 까먹을 수도 있고, 중요하게 생각하지 않을수도 있다

---

## What Not to Assert

**`Runtime assertion`은 무료가 아니기 때문에 코드를 복잡하게 만들지 않도록 신중하게 사용해야하고, 도움이 되지 않는 주석은 쓰지 않는 것 처럼 assertion도 피해야한다.**

```java
// don't do this:
x = y + 1;
assert x == y+1;
```

**→ 이러한 코드에서 assert가 명백하게 true값이 나온다면 그냥 생략 하는게 좋다.**

- **파일의 존재, 네트워크의 가용성, 사용자 입력값의 정확성과 같은 프로그램의 외부 조건을 테스트 할 때는 assertion을 사용하지 않는것이 좋다.**

→ assertion은 프로그램의 내부 상태를 검사하여 프로그램이 스펙내에 있는지 확인한다. 

- **assertion이 실패하면 제대로 작동하지 않도록 설계가 안됐다는 것을 알려준다. 즉 `Asseriton faulures` 는 버그를 나타낸다.**
- **외부에서 일어나는 장애는 버그가 아니며 사전에 프로그램에서 오류 발생 방지를 할 수 없다. 그러므로 예외처리등을 이용하여 처리해야한다.**

- **많은 Assertion 메커니즘은 테스트 및 디버깅 중에만 실행되고 프로그램이 사용자에게 릴리즈 되면 꺼지도록 설계된다.  Java에서도 이런식으로 동작한다.**

**→ 이러한 접근 방식의 장점은 프로그램의 성능을 저하시킬 수 있는 비싼 asserion를 작성 할 수 있다.**

**ex) 이진 검색을 사용하여 배열음 검색하는 프로시저에는 배열을 정렬해야하는 요구 사항이 있다.**

**→ 이러한 요구 사항을 확인하려면 전체 배열을 스캔하여 로그 시간으로 실행해야 하는 작업을 선형 시간이 걸리는 작업으로 전환해야 한다.**

**→ 테스트 하는 동안 이러한 비용을 기꺼이 지불해야한다. 이렇게 한다면 디버깅이 훨씬 쉬워지지만 프로그램이 사용자에게 릴리즈 된 후에는 그렇지 않다.**

**그러나, 릴리즈 할 때 assertion을 비활성화하는 것은 심각한 단점이 있다. assertion이 비활성화 된 경우에는 프로그램이 필요한 오류 검사를 훨씬 적게 수행한다. 보통 초코 프로그래머들은 assertion이 미치는 영향을 그들이 하는거보다 훨씬 더 걱정한다.** 

**→ 대부분에 assertion은 값이 저렴하므로 릴리즈할 때 비활성화해서는 안된다.**

**assertion이 비활성화 될 수 있으므로 프로그램이 assertion의 실행에 따라 값이 달라져서는 안된다.**

**특히 assertion문에 `side-effect` 가 존재해서는 안된다.**

**ex) 리스트에서 remove가 삭제되었는지 확인하기 위해서는 이렇게 작성하면 안된다.**

```java
// don't do this:
assert list.remove(x);
```

**이런식으로 작성한다면 assert문이 제거된다면 remove문이 실행되지 않는다.**

```java
boolean found = list.remove(x);
assert found;
// 이렇게 작성해야 x는 목록에서 제거된다.
```

---

## Incremental Development

**버그를 프로그램의 아주 작은 부분에 모아 놓는 좋은 방법 중 하나는 점진적인 개발이다.**

- **조금씩 코드를 짜고 넘어가기전에 테스트를 한다면 버그를 발견 했을 때 엄청난 양의 코드에서 버그를 찾는게 아니라 방금 작성한 부분만 보면 된다.**

테스트에 관한 수업에서 이를 수행하는데 도움되는 두가지를 다룸

1. **Unit testing** : 모듈을 분리하여 테스트 할 때 해당 모듈에 버그가 있는지 확인하거나 테스트 케이스 자체에 버그가 있는지 확인 할 수 있다.
2. **Regression testing** : 큰 시스템에 새로운 기능을 추가할 때는 회귀 테스트를 가능한 자주 실행해야 한다. 테스트가 실패한다면 방금 변경한 코드에 버그가 있을 확률이 높다.

---

 

## Modularity & Encapsulation

더 나은 소프트웨어 디자인을 위해서 버그를 `localize`(지역화) 할 수 있다.

- **Modularity(모듈화)** : 모듈화는 시스템을 구성 요소 또는 모듈로 나누는 것을 의미하며, 각 구성요소는 시스템의 나머지 부분과 별도로 설계, 구현, 테스트, 추론 및 재사용이 가능하다. 모듈형 시스템의 반대는 `monolithic system`(크기가 크고 모든 부품들이 합쳐서 서로 의존 함)단일 시스템이다.

하나의 매우 긴 main() 기능으로 구성된 프로그램은 단일화 되어 이해하기 어렵고 버그를 분리하기도 어렵다. 이 와 반대로 모듈식은 작은 기능들과 클래스로 나누어져있기 때문에 이해하기 쉽고 버그를 분리할 수 있다.

- **Encapsulation(캡슐화) :** 캡슐화는 모듈 주위에 벽을 만들어 모듈이 내부 동작에 책임을 지고 시스템의 다른 부분에 있는 버그가 손상 시킬 수 없게 만드는 것을 의미한다.
    
    
    **캡슐화의 한 종류는 `public` 와 `private` 를 사용하여 변수와 메서드의 접근성과 가시성을 제어하는 `access control` (접근 제어) 이다.**
    
    - **public 변수나 메서드는 어떤 코드에서도 접근 할 수 있다. (클래스 또한 public 일 때)**
    - **private 변수나 메서드는 동일한 클래스에 메서드로만 접근 할 수 있다.**
    
    **→ 특히 변수 같은 경우는 private 일 때 버그를 일으킬 수 있는 코드를 제한하기 때문에 캡슐화가 가능하다.**
    
    또 다른 종류의 캡슐화는 **variable scope**(변수의 범위)이다. 변수의 범위는 해당 변수가 정의된 프로그램 텍스트의 일부이다. 즉, 표현식과 명령문이 변수를 참조할 수 있다.  (메서드 매개변수의 범위는 메서드의 바디)
    
    지역 변수의 범위는 선언부터 닫는 중괄호까지이다. 
    
    변수 범위를 최대한 작게 유지하면 프로그램에 버그가 있을 수 있는 위치를 추론하기 쉽다.
    
    ```java
    for (i = 0; i < 100; ++i) {
        ...
        doSomeThings();
        ...
    }
    ```
    
    **i가 전역변수로 선언되었을 때 누군가 i 값을 변하게 만들고 있다면 i는 100에 도달하지 못한다.**
    
    ```java
    public static int i;
    ...
    for (i =0; i < 100; ++i) {
        ...
        doSomeThings();
        ...
    }
    ```
    
    **범위는 전체 프로그램이다. `doSomeThings` 메서드 혹은 doSomeThings가 호출하는 다른 메서드들, 완전히 다른 코드를 실행하는 스레드 등 프로그램 어느곳에서든 i값을 변경할 수 있다.**
    
    하지만 i가 좁은 범위 지역 변수로 선언된다면
    
    ```java
    for (int i = 0; i < 100; ++i) {
        ...
        doSomeThings();
        ...
    }
    ```
    
    **이렇게 코드를 짠다면 i값을 변경 할 수 있는 것은 …뿐이므로 `doSomeThings()` 는 이 값을 변경 할 수 없으므로 고려 할 필요조차 없다.**
    
    **Minimizing the scopre of variables(변수의 범위 최소화)** 는 버그를 지역화하기 위한 가장 강력한 실행 방법이다.
    
    - **항상 루프를 선언하기전에 변수를 선언하는 대신 for 루프문에서 선언하여라.**
    
    ```java
    int i;
    for (i = 0; i < 100; ++i) {
    ```
    
    **이렇게 코드를 짠다면 변수가 선언된 중괄호 전체이다.**
    
    ```java
    for (int i = 0; i < 100; ++i) {
    ```
    
    → 이는 i의 범위를 for 루프로만 제한한다.
    
    - **처음 필요할 때만 변수를 선언하고 중괄호 블록에서 선언해라. :** 자바의 변수 범위는 중괄호 이므로 변수를 사용헤ㅐ야하는 모든 expression을 포함하는 중괄호 가장 안쪽에 변수 선언을 한다. 함수를 시작할 때 변수를 선언하면 함수의 범위가 불필요하게 커지낟. 하지만 파이썬, 자스같은 선언이 없는 언어는 어차피 범위가 전체라서 중괄호가 범위를 제한 할 수 없다.
    - **전역 변수를 피해라 :** 프로그램이 커질수록 안좋은 생각이다. 전역 변수는 프로그램에서 매개 변수를 제공하는 용도로 사용된다. 매개 변수를 재할당할 수 있는 전역 공간에 두는 것 보다 필요한 곳에 배치하는 것이 좋다.
    
    ---
    

## ****Summary****

디버깅 비용을 최소화 하기 위한 몇 가지 방법을 알아봤다.

- **avoid debugging(디버깅을 피하라)**
    - `static typing`, `automatic`, `dynamic checking`, `imutable type and references`와 같은 기술로 버그를 불가능하게 만든다.
- **keep bugs confied(버그를 가둬라)**
    - `assertion`으로 `fail fast`를 하여 버그가 퍼지는 것을 막는다.
    - 점직적인 개발과 유닛 테스트로 버그를 최근에 쓴 코드에 가두어라
    - 범위를 최소화하여 검색해야하는 프로그램의 양을 줄인다.

**SFB :** 버그를 막고 제거하려고 노력한다.

**ETU** : `static typing` , `final declarations` 그리고 `assertions` 와 같은 기술은 코드의 가정을 위한 추가 문서화이다. 변수의 범위 최소화는 보는 코드가 적어지기 때문에 변수를 어떻게 사용하는지 이해하기 쉽게 만들어준다.  

**RFC** : `Assertions` 과 `static typing` 는 자동으로 확인 가능한 방식으로 문서화하여 프로그래머가 코드를 변경할 때 가정을 어기는지 감지해준다.