# Reading 08: Avoiding Debugging

# 목적

디버깅을 하지 않도록 프로그래밍 하는 방법, 해야한다면 디버깅을 쉽게 하는 방법에 대한 공부

# First Defense: Make Bugs Impossible

버그를 피하는 가장 좋은 방법은 버그가 발생하지 않도록 프로그램을 설계하는 것이다

## 1. Static Checking

Static Checking의 경우 Compile Time에 많은 버그를 제거할 수 있음

## 2. Dynamic Checking

`Java` 의 경우, Array Overflow를 동적으로 잡아내어  버그 발생이 불가능하도록 설계되었음

만약 `Array` 나, `List` 의 범위 밖의 인덱스에 접근하려고 한다면, `Java` 는 자동적으로 에러를 발생시킴

반면, `C`, `C++` 의 경우 별다른 에러 없이 비정상적인 접근을 허용하여 버그와 보안 취약점을 발생시킴

## 3. Immutability (immunity from change)

`immutable type` 은, 생성하면 값을 절대 변경할 수 없는 type이다

`String` 의 경우, `immutable type` 이다

`String` 에서 호출할 수 있는 메서드 중에 표현하는 characters의 sequence를 변경할 수 있는 메서드는 없다

그러므로 전달되거나 공유될 때, 다른 코드에 의해 수정될 걱정을 하지 않아도 된다

`Java` 는 `immutable reference` 도 제공하는데, 이는 한번만 할당할 수 있고, 재 할당할 수 없는 `final` 키워드로 declared된 변수를 의미한다

method parameter나 가능한 한 많은 로컬 변수를 `final` 로 declare하는 것이 좋다

이러한 Declarations는 매우 중요하고, 가독성을 향상시키고, 컴파일러가 Statically Checing할 수 있도록 한다

```java
final char[] vowels = new char[] { 'a', 'e', 'i', 'o', 'u' };
```

```java
~~vowels = new char[] { 'x', 'y', 'z' };~~
// Not Allowed
vowels[0] = 'z';
// Allowed
```

`final` 의 의미에 대하여 주의하여야 하는데,

reference를 immutable하게 만드는 것이지, reference가 가리키는 object를 immutable하게 만든다는 것은 아니다

# Second Defense: Localize Bugs

만약 우리가 버그를 막을 수 없다면, 버그의 원인을 쉽게 찾을 수 있도록 버그를 프로그램의 작은 부분으로 Localize 해야 한다

Single Method나 작은 모듈들이 Localize되면, 프로그램의 코드를 보는것만으로 버그를 쉽게 찾을 수 있다

```java
/**
 * @param x  requires x >= 0
 * @return approximation to square root of x
 */
public double sqrt(double x) { ... }
```

`sqrt` 를 누군가 음수인 argument로 호출했다고 하면,

음수가 아니어야만 하는 x의 요구조건(precondition)을 만족시키지 않았기 때문에 `sqrt` 는 더이상 `contract` 에 구속되지 않는다(이전에 했던 내용)

그렇기에 어떤 것이든 자유롭게 수행할 수 있게 된다(postcondition에 구속될 이유가 없어짐)

임의의 값을 반환하거나, 무한 반복에 빠지거나, CPU를 오류에 빠지게 만든다

그러나 잘못된 function call은 버그를 나타내기 때문에, 가장 좋은 동작은 가능한 한 빨리 버그를 지적해주는 것이다

우리는 precondition을 테스트하는 runtime assertion을 삽입하여 이를 수행한다

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

precondition이 만족되지 않았을 때, 이 코드는 `AssertionError` exception을 throw하여 프로그램을 종료시킨다

→ 호출자의 버그의 영향이 전파되는 것을 방지한다

precondition을 체크하는 것은 **defensive programming 의 한 예시이다**

실제 프로그램에서 버그가 없는 경우는 거의 없지만, **defensive programming**을 통해 버그가 어디에 있는지 모르더라도 버그의 영향을 완화할 수 있는 방법을 제공함

# Assertion

다양한 종류의 defensive check를 하기 위해 procedure를 정의하는 흔한 활동을 `assert` 라고 부름

- What is Procedure?
    
    **method that does not have a return value**
    

```java
assert (x >= 0);
```

이러한 접근 방식은 assertion fails할 때 정확히 무슨일이 일어나는지에 대하여 추상화한다

assertion이 종료될수도, 로그 파일에 이벤트를 기록할 수도, 관리자에게 이메일을 보낼 수도 있을 것임

Assertion은 프로그램의 해당 시점에 상태에 대하여 가정을 문서화하는 추가적인 이점이 있다

코드를 읽는 다른 사람들에게 `assert(x>=0)` 은 “이 시점에서 `x≥0`이면 항상 `true`여야 한다” 라고 말한다

그러나, 주석과는 달리 assertion은 runtime에 가정을 적용하는 실행가능한 코드이다

`Java` 에서 Runtime Assertion은 언어의 내장된 기능이다

단순한 형태의 assert statement는 `boolean` expression을 가지고, 이것이 `false` 일 때 `AssertionError` 을 `throw` 한다

assert statement는 또한 종종 String이지만 primitive type이거나 object의 reference인 description expression이 포함될 수도 있다

description은 assertion이 실패할 때, 에러 메시지로 출력되며 실패의 원인에 대한 추가적인 설명을 프로그래머에게 제공할 수도 있다

```java
assert (x >= 0) : "x is " + x;
```

Java assertions는 기본적으로 꺼져 있기 때문에, JVM에 -ea를 명시적으로 전달하여 활성화 해 주어야 함

```java
@Test(expected=AssertionError.class)
public void testAssertionsEnabled() {
    assert false;
}
```

다음 test case를 통하여 assertion이 활성화 되었는지 확인할 수 있음

`assert false` 가 `AssertionError` 를 발생시킬 것이고, 만약 Assertions가 활성화되어있지 않다면 `AssertionError` 를 발생시키지 않을 것이기 때문에 테스트의 결과가 실패로 나타날 것임

# What to Assert

## 반드시 Assert해야 할 몇가지 사항

- **Method argument requirement**
    - precondition
- **Method return value requirement**
    - 이러한 종류의 Assertion을 **self check**라고 부름

```java
public double sqrt(double x) {
	assert x >= 0;
	// Method argument requirement(precondition)

	double r;
	... // compute result r
	
	assert Math.abs(r*r - x) < .0001;
	// Method return value requirements
	// x의 제곱근으로 구한 값이 r인데, 이를 다시 제곱해서 원래 값인 x와의 오차를 계산
	return r;
}
```

- **Covering all cases**
    - Conditional statement가 가능한 경우를 모두 커버하지 않는 경우, assertion을 사용하여 허용되지 않는 경우를 block하는 것이 좋음

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

`vowel` 은 반드시 5개의 모음 문자 중 하나여야 한다고 주장하는 효과를 가진다

### When should you write runtime assertions?

- 코드를 작성한 후가 아닌, 코드를 작성할 때 작성하여야 함
- 코드를 작성할 때 `invariants` 를 염두에 두어야 한다
- Assertions를 작성하는 것을 연기한다면 `invariants` 를 염두할 가능성이 낮아지고, 몇가지 중요한 `invariants` 를 빠뜨릴 가능성이 높음

# What Not to Assert

Runtime Assertions를 자유롭게 사용해서는 안된다

이는 코드를 어지럽힐 수 있으므로 반드시 신중하게 사용하여야 한다

유익하지 않는 글을 피하는것 처럼, 쓸데없는 Assertions를 피해야 함

```java
// don't do this:
x = y + 1;
assert x == y+1;
```

이 Assertion은 코드에서 버그를 찾지 않는다

해당 Assertion은 버그를 컴파일러나 JVM에서 찾는데, 이것들은 의심할만한 충분한 이유가 있기 전까지는 반드시 신뢰해야 하는 구성 요소이다

Assertion이 지역적인 맥락에서 명백하다면, 이를 생략해야 한다

파일의 존재, 네트워크의 가용성, 사용자의 입력의 정확성과 같은 프로그램의 외부 조건을 테스트하는 데에 Assertion이 사용되어서는 안된다

Assertion은 프로그램 내부의 상태가 Specification의 범위 내에 있는지 확인하는 것이다

외부적인 오류는 버그가 아니며, 이를 막기 위해서 프로그램을 변경할 수 없음

Assertion의 메커니즘은 오직 테스트와 디버깅 중에만 실행되고, 사용자에게 릴리즈되면 꺼지도록 설계되었다

이러한 접근의 장점은 프로그램의 성능을 심각하게 떨어뜨리는(많은 비용이 드는) Assertion을 작성할 수 있도록 한다

```java
/**
 * @param array  requires sorted Array
 * @param element  
 * @return index of element where in array
 */
public int binarySearch(Array array, Element element) { ... }
```

- 다음과 같은 이진 탐색 Procedure가 있다고 할 때, `Array`는 반드시 정렬된 상태여야 하는 요구사항(Precondition)이 있음
- 이 프로시저의 Time Complexity는 `O(log n)` 을 가짐
- 하지만 이 Requirement(Array가 정렬된 상태여야 함)을 Asserting하려면, 정렬된 상태인지 확인하는 Validation 작업이 필요함
- Validation의 시간 복잡도는 `O(n)` 을 가짐 (모든 요소를 확인하여야 함)
- 결국 이 작업은 `O(log n)` 을 `O(n)` 으로 시간복잡도를 바꾸는 결과를 초래함
- 이는 디버깅이 훨씬 쉬워지지만, 추가 비용(성능)이 요구됨

즉, 프로그램을 개발할 때에는 성능보다는 정확선을 우선시하여야 함

하지만 이러한 검증 작업은 release 단계일 때에는 더이상 필요하지 않으며, 사용자는 빠른 속도를 기대하기 때문에 `O(log n)` 의 시간복잡도를 가지는 알고리즘을 원할 것임

release 단계 에서는 성능 최적화에 집중하여야 함

### 그러나,

Release에서 Assertion을 비활성화 하는 것은 심각한 단점이 존재하는데, 프로그램이 이를 가장 필요할 때 오류 검사를 훨씬 적게 수행하는 것이다

초보 프로그래머들은 Assertion으로 인해 발생하는 성능 저하에 대하여 과하게 걱정하는 경향이 있다

하지만 대부분의 Assertion으로 인한 성능 저하는 생각보다 높지 않으므로 공식 release에서 이를 비활성화 하지 않아야 한다

Assertion이 비활성화될 때, 프로그램의 정확성은 Assertion Expression이 실행되는지에 따라 의존되지 않아야 한다

특히, Assertion Expression은 (side-effect)부작용이 없어야 한다

예를 들어, 리스트에서 찾은 요소를 제거하는 Assertion의 경우에 다음과 같이 작성하지 않아야 한다

```java
assert list.remove(x);
```

만약 Assertion이 비활성화된 경우, 전체 식을 건너뛰고 `x` element는 리스트에서 제거되지 않을 것이다

```java
boolean found = list.remove(x);
assert found;
```

# Incremental Development

Incremental Development는 프로그램의 아주 작은 부분으로 버그를 지역화하는 좋은 방법이다

한번에 프로그램의 일부만 빌드하고, 다음 단계로 넘어가기 전 철저히 테스트한다

그렇게 하면 버그를 발견했을 때 거대한 코드의 더미 어디에 있는 것보다, 방금 작성한 부분에 있을 가능성이 높다

### Unit testing

독립적으로 모듈을 테스트할 때, 찾은 어떠한 버그든 Unit 안에 혹은 테스트 케이스 자체에 존재할 것임을 확신할 수 있음

### Regression testing

큰 시스템에 새로운 기능을 추가할 때, Regression test suite를 가능한 한 종종 실행시켜라

만약 테스트가 실패하면, 버그는 아마 방금 바꾼 코드 안에 있을 것이다

- Regression testing
    
    이전에 작동하던 소프트웨어 기능에 문제가 생기는 것을 가리킨다
    
    이전의 실행 테스트를 재 실행하며 이전에 고쳐졌던 오류가 재현되는지 검사하는 방법이 많이 사용됨
    
    XP(익스트림 프로그래밍)의 중요한 부분이다
    

# Modularity & Encapsulation

더 나은 소프트웨어 설계 또한 버그를 지역화시킬 수 있음

## Modularity

Modularity는 시스템을 component, 모듈로 나누는 것을 의미하고 각각은 시스템의 나머지 부분과는 별개로 설계, 구현, 테스트, 추론 및 재사용할 수 있음

Modular system의 반대는 Monolithic(단일) system이다

단일 시스템은 크기가 크고, 모든 부분들이 각각 뒤엉키고, 의존하고 있음

모든 프로그램이 `main()` 함수에 단일화 되어있다면, 이는 이해하기도 어렵고 버그를 분리하기도 어려움

대조적으로, 작은 함수와 클래스들로 나누어진 프로그램은 더 모듈식이다

## Encapsulation

Encapsulation은 모듈 주변에 벽을 쌓는 것을 의미하는데, 모듈이 자신의 내부적인 동작에 책임이 있도록 하고 시스템의 다른 부분의 버그가 그것의 무결성에 손상을 줄 수 없도록 하는 것을 의미함

encapsulation의 한 종류는 Access control인데, `public` 과 `private` 를 사용하는 것은 변수나 메서드의 visibility와 accessibility를 제어하는 것이다

public 변수나 메서드는 어느 코드(변수나 메서드를 포함하는 클래스 또한 public이라고 가정)에서나 접근할 수 있다

private 변수나 메서드는 같은 클래스 내의 코드로만 접근할 수 있음

특히 변수에 가능한 private로 지정하는 것은 encapsulation을 제공하는데, 의도치 않게 버그를 발생시키는 코드를 제한하기 때문이다

또다른 encapsulation은 Variable Scope에서 나온다

변수의 범위는 해당 변수가 defined된 프로그램 텍스트의 일부분을 의미하는데, 이는 Expression과 Statements가 변수를 참조할 수 있는 범위이다

Method parameter의 범위는 Method의 body이다

지역 변수의 범위는 해당 변수가 선언된 곳부터 다음 닫는 중괄호 까지이다

변수 범위를 가능한 한 작게 유지하는 것은 프로그램에서 버그가 어디에 있을 지 이해하기 훨씬 쉽게 만든다

```java
for (i = 0; i < 100; ++i) {
    ...
    doSomeThings();
    ...
}
```

만약 이 프로그램에서 i가 100에 도달하지 않는 무한 반복되는 오류를 발견했을 때,

어디선가, 누군가가 i를 변화시키고 있다. 하지만 어디에서인지 알 수 없다.

만약 i가 전역 변수로 declared 되어 있다면?

```java
public static int i;
...
for (i =0; i < 100; ++i) {
    ...
    doSomeThings();
    ...
}
```

이것의 범위는 프로그램 전체이다

하지만, `i` 를 지역변수로 선언한다면

```java
for (int i = 0; i < 100; ++i) {
    ...
    doSomeThings();
    ...
}
```

`i` 가 변화될 수 있는 곳은 오직 `for statement` 내부이다

`doSomeThings()` 는 해당 지역 변수에 접근할 수 없기 때문에, 생각하지 않아도 되는 것이다

변수의 범위(Scope)를 최소화하는 것은 버그를 지역화하는 가장 강력한 방법이다

- **Always declare a loop variable in the for-loop initializer**

```java
~~int i;~~
for (i = 0; i < 100; ++i) {
// rather than declaring it before the loop
for (int i = 0; i < 100; ++i) {
```

- **Declare a variable only when you first need it, and in the innermost curly-brace block that you can**

`Java` 에서 변수의 범위는 중괄호 block이다

그래서 변수의 declaration을 변수를 사용해야 하는 모든 expression을 포함하는 가장 안쪽에 한다

함수를 시작할 때 모든 변수를 선언하지 않아야 한다

함수의 범위가 불필요하게 커진다

- ********Avoid gloabl variables********

프로그램이 커질 수록, 전역 변수는 종종 프로그램의 여러 부분에 parameter를 제공하는 곳에 사용된다

parameter를 실수로 재할당 할 수 있는 전역 공간에 두는 것보다 필요한 코드에 전달하는 것이 좋다

# Summary

- Avoid Debugging
    - 기술을 사용하여 버그 발생이 불가능하게 만든다
    - Static Typing, Automatic Dynamic Checking, Immutable types and references
- Keep bugs confined
    - Assertion을 통한 Failing fast로 버그의 영향이 퍼지는 것을 막는다
    - Incremental development와 Unit testing으로 버그를 최근 작성한 코드로 지역화한다
    - Scope minimization으로 프로그램에서 찾아야 하는 양을 줄인다

- **Safe from bugs.** We’re trying to prevent them and get rid of them.
- **Easy to understand.** Techniques like static typing, final declarations, and assertions are additional documentation of the assumptions in your code. Variable scope minimization makes it easier for a reader to understand how the variable is used, because there’s less code to look at.
- **Ready for change.** Assertions and static typing document the assumptions in an automatically-checkable way, so that when a future programmer changes the code, accidental violations of those assumptions are detected.