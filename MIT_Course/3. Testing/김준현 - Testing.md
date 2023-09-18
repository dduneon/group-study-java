# Reading 3: Testing

# 서론

---

## 우리의 목표

- 버그로부터 안전한 코드를 짜는 것
  - 현재에도, 미래에도 문제 없을만한 코드
- 이해하기 쉬운 코드를 짜는 것
  - 다른 사람이 보았을 때, 명확하게 이해할 수 있는 코드
- 변화에 적응할 수 있는 코드를 짜는 것
  - 재작성 하지 않고, 변화를 수용할 수 있는 코드

## 이번 Chapter의 목표

- 테스트의 가치를 이해하고, Test-first programming의 과정을 아는 것
- input, output 공간을 분할하고 좋은 테스트 케이스를 선택하여 method에 대한 test suite를 설계할 수 있는 것
- code coverage를 측정하여 test suite를 판단할 수 있는 것
- 블랙박스 테스트와 화이트박스 테스트를 언제 사용하는지, 그리고 이해하는 것. Unit Test와 Integration Test, 그리고 Automated Regression Test를 이해하는 것.

# Validation

> Testing은 검증이라고 부르는 일반적인 과정의 한 예시이다. 검증의 목적은 프로그램의 문제점을 찾아내어 프로그램의 정확성에 대한 신뢰를 높이는 것이다.

---

## Validation includes

- Formal reasoning(형식적인 추론)
  - 검증은 프로그램이 정확하다는 공식적인 증거를 구성함
  - 이는 수작업으로 진행되기 때문에 지루하고, 검증을 위한 자동화된 도구 지원은 활발한 연구 분야이다
  - 운영체제의 스케줄러, 가상머신의 바이트코드 인터프리터, 운영체제의 파일시스템과 같이 프로그램의 작고 중요한 부분들이 검증될 수 있다
- Code Review
- Testing

Typical residual defect rates(소프트웨어가 출고되고 남은 버그) per kloc(소스코드 천 줄당)

- 1-10 defects/kloc: 일반적인 산업용 소프트웨어
- 0.1 - 1 defects/kloc: 고품질 검증, Java Library
- 0.01 - 0.1 defects/kloc: 최고의 안전에 중요한 검증, Nasa …

# Why Software Testing is Hard

---

## 완벽한 테스트는 불가능하다

- 가능한 테스트 케이스의 범위는 너무 커서 모든 경우를 테스트할 수 없다

## 무작정 테스트 하는것은 옳지 않다

- 프로그램의 버그가 너무 많아서 성공할 확률보다 실패할 확률이 많은 경우가 아니라면 “그냥 시도해 보고 작동하는지 확인하는 것” 은 버그를 발견할 가능성이 적음
- 또한, 프로그램의 정확성에 대한 신뢰를 높여주지 않는다

## 무작위, 통계적 테스트는 소프트웨어에 잘 적용될 수 없음

- 다른 분야에서는 작은 무작위 경우를 추출하여 전체 생산품의 불량률을 추론할 수 있다
- 하지만, 소프트웨어의 동작은 가능한 입력의 경우에 따라 불연속적이고 개별적으로 달라진다

# Putting on Your Testing Hat

---

### 코드를 작성할 때의 목표는 프로그램이 작동되도록 만드는 것이 목표이지만, 테스트할 때의 목표는 실패하게끔 만드는 것이 목표이다

- 자신이 쓴 코드를 너무 소중하게 생각하여 가볍게 테스트하는 것은 좋지 않다
- 좋은 테스트는 프로그램의 취약점이 있을 수 있는 모든 곳에서 테스트하고, 취약점을 제거할 수 있도록 해야한다

# Test-first Programming

> 일찍, 자주 테스트하라

---

코드가 많이 쌓인 경우에는, 버그가 코드 어디에나 존재할 수 있기 때문에 debugging이 길어지고 힘들어진다

따라서 코드를 개발할 때 테스트하는 것이 훨씬 좋다

`test-first-programming` 에서는 코드를 작성하기 전에 테스트를 작성한다

1. 함수의 specification을 작성한다
2. specification을 실행하는 테스트를 작성한다
3. 실제 코드를 작성한다. 코드가 테스트를 통과하면 완료된다

### Specification?

- `input`, `output` 동작에 대해 설명한다.
- parameter types, 추가적인 constraints(`sqrt` 의 parameter는 음수가 아니어야 함)
- return value type, return value와 input의 연관성

## 결론

- Test-first-programming은 specification을 이해하는 좋은 방법중 하나이다.
- 하지만 specification 또한 버그가 존재할 수 있음(incorrect, incomplete, ambiguous, missing corner case)
- 버그가 존재하는 specification을 구현하는 데에 시간을 낭비하기 전에, 빠르게 이러한 문제를 발견할수 있도록 테스트를 작성하라

# Choosing Test Cases by Partitioning

> Small enough to run quickly, yet large enough to validate the program

---

- 입력 공간을 `subdomains`로 나누고, 각각의 입력 집합으로 구성
- 모든 입력이 적어도 하나의 `subdomains`에 있도록, `subdomains`를 모두 합하면 입력 공간이 완전히 포함되도록
- 각 `subdomain` 에서 하나의 `test case` 를 선택하면 그것이 `test suite` 이다
- 제한된 테스트 자원을 최대한 활용함

## Example: BigInteger.multiply()

> BigInteger? Java library, 정수를 크기 제한이 없이 나타낼 수 있다

```java
public BigInteger multiply(BigInteger val)

BigInteger a = ...;
BigInteger b = ...;
BigInteger ab = a.multiply(b);
```

`multiply : BigInteger x BigInteger -> BigInteger`

→ two input argument(BigInteger), output type is BigInteger

### (1) Input space를 두개의 차원으로 나눈다

- (a, b): 한 쌍의 정수

### (2) 이를 partitioning 한다

- a와 b는 둘다 양수
- a와 b는 둘다 음수
- a는 양수, b는 음수
- a는 음수, b는 양수

### (3) 곱셈의 특별한 경우를 고려

- a나 b가 0, 1, -1 인 경우

### (4) BigInteger의 내부적인 구조에 대한 추측

BigInteger가 작은 수(`int` , `long` )로 표현할 수 있는 계산인 경우에는, 해당 `primitive type` 을 사용하여 연산을 실행하지 않을까? → 추측

- a나 b가 작은 수 인 경우 (`int`, `long`)으로 표현할 수 있는 경우
- a나 b가 표현할 수 있는 범위를 초과한 값(`Long.MAX_VALUE`)(정수의 `primitive type` 으로 표현할 수 있는 가장 큰 수) 을 가지는 경우

### (5) 전체 경우의 수를 나열한다

- 0
- 1
- -1
- 작은 양수(4에서 `primitive type` 으로 표현될 수 있는 범위의 수)
- 작은 음수
- 큰 양수(`primitive type` 으로 표현할 수 있는 범위를 초과한 값)
- 큰 음수

⇒ 총 7가지의 `a` 와 `b` 의 경우의 수

⇒ 총 49개의 `partition` 이 만들어 진다 (= `subdomains`)

![스크린샷 2023-09-14 오후 4.39.11.png](./image/dduneon/untitled 3.png)

### (6) 해당 subdomain들(partition) 범위를 만족하는 하나의 test case를 각각 지정한다

## Example: max()

```java
/**
 * @param a  an argument
 * @param b  another argument
 * @return the larger of a and b.
 */
public static int max(int a, int b)
```

`max: int x int -> int`

- a > b
- a = b
- a < b

⇒ 총 3가지 partition이 생기고, 각각 범위 내의 test case를 지정하면 test suit가 된다

![Untitled](./image/dduneon/Untitled.png)

# Include Boundaries in the Partition

> 버그는 종종 `subdomains` 들의 경계에서 발생한다

- 0은 음수와 양수의 경계값이다
- `int` 나 `double` (numeric type)의 최대, 최소 값
- `Collection Types` 의 emptiness (빈 문자열, 빈 리스트, 빈 배열)
- `Collection` 에서의 첫번째와 마지막 요소

### Why do bugs often happen at boundaries?

- 프로그래머들은 종종 실수를 한다
  - `<=` 대신에 `<` 를 쓴다던지, `counter` 을 0이 아닌 1로 초기화 한다던지 등.
- 일부 boundaries들은 코드의 특수한 경우로 처리하여야 한다
- boundaries가 코드의 동작에 불연속적일 수 있다
  - `int` 의 경우 최대 양수의 범위보다 더 초과할 수 있다
  - 예를 들면 갑자기 음수가 된다

![Untitled](./image/dduneon/Untitled%201.png)

## Two Extremes for Covering the Partition

> Input space를 partitioning 하고 나서, 우리는 우리가 원하는 test suite를 어떻게 완전하게 만들것인지 결정할 수 있다

### Full Cartesian Product

- 위 예시의 `multiply` 의 경우, 7 x 7 = 49의 테스트 케이스를 만들었다
- 만약 boundaries를 포함한 `max` 의 경우에서, 차원이 하나 추가되어 (3경우 x 3경우 x 5경우) 가 된다면?
  - 이는 총 75개의 test case가 존재함을 의미하고, 하지만 이에 결점도 존재한다
  - `a<b` , `a=0`, `b=0` 인 경우 모든 조건을 만족할 수 없음

### Cover each part

- 각각의 차원에서의 모든 part(경우의 수)는 적어도 하나의 test case로 검증될 수 있음
- 하지만, 모든 조합에 대해 반드시 필요하지는 않음
- `max` 에 대한 test suite 에서 신중히 선택한다면 5개 정도의 test case를 선택할 수 있음

화이트박스 테스트와 code coverage tools의 영향과 인간의 판단과 신중함을 바탕으로 두 개의 경우 사이에서 어느 정도 타협 한다

![Untitled](./image/dduneon/Untitled%202.png)

# Blackbox and Whitebox Testing

> 위에서 설명한 specification은 parameter들의 type, return value, 그들간의 제약조건이나 관계와 같은 함수의 동작에 대한 설명이다

## Blackbox Test

- 함수의 구현을 보지 않고, 오직 specification만으로 test case를 고르는 것
- 위에서 우리가 예시를 든 것과 같음
  - `multiply` 와 `max` 처럼 실제 함수의 코드를 보지 않고 경계를 찾았다

## Whitebox Test

- 실제 함수 구현되는 방법을 알고, test case를 선택하는 것
- 구현이 입력에 따라 서로 다른 알고리즘을 선택하는 경우 해당 도메인에 따라 분할해야 함
- 이전 입력에 대한 답변을 기억하는 경우, 반복 입력을 테스트

화이트박스 테스트를 진행할 때, 프로그램의 specification이 특별히 요구하지 않는 특정 구현 동작을 테스트하지 않도록 주의해야 한다

- 입력 형식이 불량한 경우 예외 처리 라고 specification에 명시되어 있는 경우

→ 이 경우에는 NullPointerException을 체크하지 않아도 된다.

- 왜냐? 이것은 이미 현재 구현 방식이기 때문이다
- specification이 어떤 exception이 발생해도 허용할 수 있도록 되어있기 때문에, 구현자의 자유를 보장해 주기 위해야 한다

# Coverage

> test suite를 판단하는 한가지 방법은, 프로그램이 얼마나 철저하게 실행되는 지 알아보는 것

---

### Statement Coverage

- 모든 statement가 몇가지 test case에 의하여 실행되는지 판단

### Branch Coverage

- 프로그램의 모든 `if` 나 `while` statement에서 `ture` 와 `false` 방향 둘 다가 몇몇의 test case에 의하여 실행되는지

### Path Coverage

- 모든 가능한 분기의 조합(프로그램의 모든 경로)를 몇몇의 test case에 의하여 실행되는지?

- Branch Coverage는 Statement Coverage에 비해 더 많은 테스트가 필요하며, Path Coverage는 Branch Coverage보다 더 많은 테스트가 필요하다
- **Path Coverage > Branch Coverage > Statement Coverage**

### 현업에서, 100%의 Statement Coverage는 흔한 목표이다.

- 하지만, 도달할 수 없는 코드(”should never get here”) 과 같은 defensive code에 의하여 목표를 흔히 달성하지 못한다.
- 100% Branch Coverage는 매우 바람직하고, safety가 중요한 industry code는 훨씬 더 엄격한 기준(MCDC, modified decision/condition coverage) 이 있다
- 100%의 Path Coverage는 기하급수적인 test suite가 필요하기 때문에 적용이 불가하다

- 테스트에 대한 표준 접근 방식은 test suite가 적절한 statement coverage를 달성할 때 까지 테스트를 추가하는 것
- 프로그램에서 도달 가능한 명령문이 최소한 하나의 테스트 케이스에 의해 실행될 때 까지
- Statement Coverage는 종종 Code coverage tool(test suite에 의해 실행되는 각각의 statement의 개수를 체크)로 측정된다

이러한 도구를 이용하면, whitebox testing은 매우 쉽다

- 단지 black box test의 coverage를 측정하고, 모든 중요한 statement가 실행되었다고 기록될 때 까지 test case를 추가하면 된다

# Unit Testing and Stubs

> 잘 테스트된 프로그램은 포함하는 모든 독립적인 모듈들에 대한 테스트를 가지고 있다

---

Unit Test는 가능하다면 개별적으로 개별적인 모듈을 테스트하는 것이다

- 모듈을 분리하여 테스트하면, 디버깅이 훨씬 쉬워진다
- 왜냐하면, 모듈에 대한 unit test가 실패하면, 해당 모듈에서 버그가 발생한다는 것을 확신할 수 있기 때문

Integration Test는 모듈의 조합, 전체 프로그램을 테스트 하는 기법

- 테스트가 실패하면, 버그를 찾아내는 과정이 필요함
- 이 버그는 프로그램 어디든 존재할 수 있음
- 하지만, Integration Test 또한 모듈 간 연결에서 프로그램이 오작동 할 수 있기 때문에 여전히 중요함

```java
/** @return the contents of the web page downloaded from url
 */
public static String getWebPage(URL url) {...}

/** @return the words in string s, in the order they appear,
            where a word is a contiguous sequence of
            non-whitespace and non-punctuation characters
 */
public static List<String> extractWords(String s) { ... }
```

```java
/** @return an index mapping a word to the set of URLs
            containing that word, for all webpages in the input set
 */
public static Map<String, Set<URL>> makeIndex(Set<URL> urls) {
    ...
    for (URL url : urls) {
        String page = getWebPage(url);
        List<String> words = extractWords(page);
        ...
    }
    ...
}
```

여기서 우리가 예상하는 Test Suit는 다음과 같다

- `getWebPage()` 에 대하여 다양한 URL들로 테스트하는 Unit Test
- `extractWords()` 에 대한 다양한 String들로 테스트하는 Unit Test
- `makeIndex()` 에 대한 다양한 URL들로 테스트하는 Unit Test

### `extractWords()`

일반적으로 해당 메서드가 실제 프로그램에 사용될 때, `getWebPage()` 의 결과물을 인자로 받는다

하지만 이를 테스트할 때에는, 이를 독립적으로 테스트 하여야 한다

- `extractWords()` 는 실제 프로그램에서 사용되기 때문에 test partition을 사용하면 web page content가 reasonable 해짐
- 하지만 `getWebPage()` 를 test case에서 호출하지 않는것이 중요
- 이는 해당 메서드에 버그가 많을 지도 모르기 때문
  - 대신 직접적으로 literal string으로 저장된 web page content를 `extractWords()` 로 넘겨주는 것이 좋다

### `makeIndex()`

해당 메서드는 쉽게 독립될 수 없음

해당 코드의 정확성은 단순 이 메서드의 코드들 뿐만 아니라, 이 메서드에 의해 호출되는 함수들까지 확인하여야 하기 때문이다

- 따라서 우리는 `getWebPage()` 와 `extractWords()` 를 위한 개별적인 테스트를 진행하여, `makeIndex()` 의 문제를 지역화 할 수 있다

또한, `makeIndex()` 와 같은 상위 모듈을 독립시키는 것은, 메서드가 호출하는 모듈의 `stub version` 을 작성하는 것으로 가능함

- `getWebPage()` 에서 인터넷에 접속하지 않고 특정 URL이 전달되는 `mock web page` 를 반환함
- 클래스를 위한 `stub` 을 `mock object` 라고 부름
- `stub` 은 큰 시스템을 설계할 때 중요한 기술임

# Automated Testing and Regression Testing

> Automated Testing은 테스트와 결과를 검사하는 것을 자동적으로 실행하는 것이다

---

### 조건

테스트 드라이버는 Interactive program 이어서는 안된다

- Interactive Program이란, 입력을 사용자에게 요구하고 결과를 출력하여 사용자가 수동적으로 체크를 하도록 요구하는 프로그램을 말함
- 테스트 드라이버는 모듈 자체를 고정된 테스트 케이스를 가지고, 결과가 정확한지 자동적으로 체크해야 함
- 또한, 이의 결과는 “모든 테스트가 통과되었습니다” 혹은, “~~ 테스트가 실패하였습니다” 이어야 함

Automated Testing이 있으면, 코드를 수정하였을 때마다 테스트를 실행하는 것이 매우 중요함

- 이는 프로그램이 `regression`하는 것을 막아 줌
- `regression` 이란, 새로운 버그를 수정하였거나 새로운 기능을 추가하였을 때, 또다른 버그가 발생하는 것
- 모든 변경 이후 테스트를 실행하는 것을 `regression testing` 이라고 한다

### Regression Testing

버그를 찾아서 고칠때마다, 버그를 발생시킨 입력을 받아 테스트 케이스를 자동화된 test suite에 추가한다

이렇게 하면, test suite를 좋은 테스트 케이스들로 채울 수 있다

- 모든 regression test가 코드의 한 버전(새로운 기능을 추가하거나, 버그를 수정하였을 때)에 수행되었다.
- 이미 한번 이상 발생했다면 쉽게 또 다시 발생할 수 있고, Regression Testing으로 이 버그가 다시 발생하는 것을 방지할 수 있음

### Test First Debugging

버그가 발생하면, 즉시 테스트 케이스를 발생한 버그에 맞게 작성하고 즉시 test suite에 추가한다

- 버그를 찾고 고치면, test case들이 passing될 것이고, debugging과 버그에 대한 regression test를 끝마친 것임
- regression testing은 테스트를 자주 자동적으로 실시할 수 있는 경우에만 유용하다.
- 따라서 automated regression testing은 현대 소프트웨어 엔지니어링의 모범 사례이다

# Summary

---

- Test-first programming
  - 코드를 작성하기 전, 테스트를 작성하는 것
- 테스트 케이스를 체계적으로 선택하기 위한 경계 설정과 파티셔닝
- 화이트 박스 테스트와 Test suite를 채우기 위한 Statement coverage
- 가능한 한 개별적인 각각의 모듈에서의 unit testing
- 버그가 다시 발생하지 않도록 하는 automated regression testing

### Safe from bugs

테스팅은 코드에서 버그를 찾는 것이고, test-first programming은 그것들을 가능한 한 빨리 찾고, 즉시 버그들을 보여주는 것이다

### Easy to understand

테스팅은 코드 리뷰만큼 도움을 주지 않음

### Ready to change

spec의 동작에 의존하는 테스트를 작성함으로써 ready-for-change할 수 있음

또한, automated regression testing에서 코드에 변화가 발생하면, 버그가 다시 발생하는 것을 막는다고 이야기 함
