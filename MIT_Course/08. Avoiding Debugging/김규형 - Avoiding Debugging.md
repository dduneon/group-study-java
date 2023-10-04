# Avoiding Debugging(23.09.25)

## ****First Defense: Make Bugs Impossible****

- 버그에 대한 최고의 방법은 설계상 불가능하게 하는것.
- 이전에 이야기한 static checking → 컴파일 시간에 버그를 제거함

- **불변성** : 한번 생성된 값은 절대 바뀌지 못한다. → 버그를 예방하는 또 다른 원리
- String은 불변이다.버그를 방지하기 위한 디자인중 하나이다.
- String을 호출 할때 해당 String이 변화 할것임을 걱정하지 않아도 된다.
- 자바는 또한 불별 참조를 제공한다. `final` 예약어를 통해서 절대 재할당 하지 못하게 막을 수 있다.
- parameter에 `final` 을 선언하는 것은 상당히 유효하다.
- 변수의 유형처럼 선언은 중요한 문서입니다. 컴파일러가 static checking 할 수 있게 도와줄수 있기에.
- final은 불변 객체이다. 하지만 다음과 같은 예시는?
    - `final char[] vowels = new char[] { 'a', 'e', 'i', 'o', 'u' };`
    - `vowels = new char[] { 'x', 'y', 'z' };`
    - `vowels[0] = 'z';`
    - final은 참조를 불별하게 만들 뿐이다.
    - 즉 참조가 가리키는 개체를 보장하진 않는다.
- 또 다른 예시
    
    ```java
    char[] vowel4 = new char[] { vowel0, 'e', 'i', 'o', 'u' };
    final char[] vowel5 = vowel4;
    
    vowel4[0] = 'x';
    vowel5[0] = 'z';
    
    vowel4 = "zeiou"
    vowel5 = "zeiou"
    ```
    
    - `voewl4`와 `vowel`5 는 같은 배열 객체를 가르키고 있기에 값이 같이 변경된다.

## ****Second Defense: Localize Bugs****

- 버그를 방지하지 못할 때, 프로그램의 작은 부분으로 지역화 시켜서 우리가 버그를 찾기 어렵지 않도록 설정해야한다.
- 메소드를 작게 하거나 모듈화 할 때 버그는 프로그램 텍스트를 학습 하는것으로 버그를 찾을 수 있다.
- 이건 **fall fast** 와도 비슷하다.
- 예시
    
    ```java
    /**
     * @param x  requires x >= 0
     * @return approximation to square root of x
     */
    public double sqrt(double x) { ... }
    ```
    
    - 만약 `sqrt` 를 호출 할때 `x` 인자를 -1을 주었다고 가정하자.
    - `sqrt` 는 모호한 값을 return 하거나, 무한 루프에 빠질 수 있다.
    - 가장 유효한 방법은 버그를 빠르게 탐지해서 이를 지적하는것 이다.
    - 우리는 `Assertion` 을 사용 할 수 있다.
    
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
    
    - **Precondition**이 충족 되지 않으면 코드는 Assertion 에러를 던진다.
    - 이러한 효과는 호출자가 버그의 영향이 전파되는걸 방지한다.
    - 이러한 precondition을 체크하는 것은 방어적 프로그래밍이라고 한다.
    - 이런 방어적 프로그래밍은 버그의 영향을 안화할 수 있게 제공한다.
    

## Assertions

- 일반적으로 프로시저의 다양한 방어 점검을 할때 사용하는 것은 `assert` 라고 한다.
- `assert (x >= 0);`
- 이러한 접근은 실패할 때 정확히 발생하는 방식이다.
    - 주로 실패한 곳에서 로그 파일에 기록한다. → 유지보수자에게 이메일로알림
- 또한 Asserstions은 프로그램의 상태에 대한 가정을 문서화하는 이점을 갖는다.
- `assert (x >= 0);` 는 이 순간에 x는 항상 참이여야 한다는 걸 알인다.
- 주석과는 다른것이 이건 실제 코드에서 강제한다.
- 이건 Java 언어의 내장된 기능이다.
- 간단하게 boolean을 사용해서 AssertionError를 던진다.
- 주로 Assertion문은 식으로 묘사되며 대게 스트링으로 다뤄지지만, 원시 타입 및 객체에 대한 참조일 수도 있다.
- Asserstion의 실패는  에러 메세지로 print 되며 이것은 프로그래머에게 실패를 야기 할수 있는 추가적인 정보를 제공하는데 사용될 수 있습니다.
- 이 expression은 콜론으로 분리됩니다. 예시는 아래와 같아요
    - `assert (x >= 0) : "x is " + x;`
- 만약 x == -1 이라면 이렇게 나온다
    - x is -1
- 이와 함께 스택을 추적해서 프로그램 안의 코드나 sequence 호출 등 어디서 assert 에러를 찾았는지 위치를 말해줍니다.
- 이러한 정보는 버그를 찾기에는 충분한 정보
- 일반적으로 Assertion은 해제되어 있다. → 비용 문제
- 하지만… 일반적으로 Assertion을 통해 얻는 이득이 더 크다!
- *-ea* 옵션을 VM 인수에 넣어 활성화 한다.
- JUNIT 테스트시에 Assertion을 키고 실행하자 케이스의 예시
    
    ```java
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false;
    }
    ```
    
- `Assertions false` 일 경우에 AssertionError를 thorws 한다.
- 자바의 `assert` 와 JUnuit의 `assertTrue()` 와 `assertEquals()`  는  서로 다르다.
- 그들은 모두 사용자의 코드에서 다른 문맥만을 비교한다.
- `assert` 문은 구현된 코드 내에서 방어적 점검을 위해 사용된다.

## What to Assert

- **Assert** 를 활용한 `sqrt`
    
    ```java
    public double sqrt(double x) {
        assert x >= 0;
        double r;
        ... // compute result r
        assert Math.abs(r*r - x) < .0001;
        return r;
    }
    ```
    
    - 메소드 인자에 대한 요구 사항
    - 메소드 리턴값에 대한 요구 사항 →자기 점검
    
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
    
    - 모든 사례를 테스트하기
- 런타임 assertions을 언제 작성해야할까?
    - 코드를 다 쓰고 난 뒤가 아닌… 코드를 적으면서 적자 (잊어먹으니깐)

## What Not to Assert

- Runtime assertions은 무료가 아니다.
- 그것들은 코드를 어수선하게 만들 수 있기에, 남발해서는 안된다.
- 사소한 assertions은 생략한다.
    
    ```java
    // don't do this:
    x = y + 1;
    assert x == y+1;
    ```
    
- 이 assert는 버그를 찾을 수 없는 대표적인 예
- 모든것을 의심하지 말고 믿을건 믿는 방식이 필요
- 프로그램 외부의 조건( 사람의 입력, 네트워크 상태) 에는 assertion을 사용하지 않는다.
- assertion은 프로그램 내부의 상태에서 보장한다. 명세의 내부에 있는지에 대해
- assertion 실패할 경우 프로그램은 제대로 작동하지 않은 경우를 말한다.
- 그러므로 assertion의 실패 = 버그 존재 라고 볼수 있다.
- 외부의 장애는 Exceoption으로 처리한다.
- 많은 assertion의 메커니즘은 이걸 테스팅이나 디버깅동안 실행하기고, 유저에게 릴리즈 할때는 끄는게 원칙이다.
- 자바의 assert는 이러한 길을 자기오 있다.
- 이러한 접근법의 이득은 당신의 프로그램이 매우 비싼 assertion을 작성하게 도운다.
- 예를 들어 이진 탐색을 통해 탐색 하는 프로시저가 있을때, asserting은 전체 배열을 스캔할 수 있다. 그러나 이런 방식은 긴 시간이 걸린다.
- 테스팅 동안은 그런 비용을 지불할수 있다. 왜냐하면 디버깅을 쉽게 해주니깐! 그러나 릴리즈 후에는 이러한 테스트는 불가능하다.

- 그러나 release 할 동안 assertion을 비활성화 하는 것은 심각하게 손해이다. 그러나 assertion이 가져다 주는 이득은 강력하기에 전부 지우지는 않는다.
- assertion이 비활성화될 프로그램내에서 의존이 존재 하면 안된다.
- 특히 side-effect가 없어야한다.
    - 리스트 안에서 요소를 삭제하는 expression
    
    ```java
    // don't do this:
    assert list.remove(x);
    ```
    
    - assertion이 비활성화 된다면, 전체 식이 실행되지 않는다. 그리고 x는 절대 식에서 제거 되지 않는다.
    - 이렇게 바꿀것.
    
    ```java
    boolean found = list.remove(x);
    assert found;
    // good!
    ```
    

## Incremental Development

- 지역화된 버그릐 최적의 길은 점진적으로 조금씩 부분으로 개발하는것이다.
- 작은 프로그램만 빌드하고, 철저한 테스트
- 이러한 방법은 버그를 찾을때 이전의 더미 보다 방금 짠 부분에서 버그가 있을 확률이 높다.
- 자세한 내용은 사전의 test에…
    - Unit Testing
    - Regression Testing

## Modularity & Encapsulation

- 좋은 소프트웨어 디자인을 위해 버그를 지역화 시킬 수 있다.
- **Modularity**
    - 모둘화는 시스템의 컴포넌트, 모둘 단위로 설계, 구현, 테스트 등으로 각각 별도로 기능하도록 나누는 것을 의미한다.
    - 그리고 각 시스템을 반복적으로 재사용한다.
    - 모듈 시스템의 반대는 모노톨릭(단일) 시스템이다.
    - 서로가 뒤영켜 의존하는 상태
    - 하나의 긴 main()이 있기에 이해하기 어렵고… 버그를 찾기가 힘들다.
- **Encapsulation**
    - 캡슐화는 모듈의 외각에 벽을 짓는것과 같다.
    - 모듈의 내부 행동, 버그, 작은 문제가 다른 모듈에게 데미지가 가지 않도록 모듈의 외곽에 벽을 세우는것과 같다.
    - 이런 캡슐화는 접근 컨트롤의 종류중 하나로 사용되는데 `pulic` 과 `private` 는 모듈(메소드, 변수)의 가시성과 접근성을 조종 가능하다.
    - `public` 변수와 메소드는 접근 가능한데 코드의 어떤 곳에서든지.
    - `private` 는 오직 같은 클래스 내에서만 접근 가능하다.
    - 가능한한 `private` 를 유지하자… 특히 변수에 대해서 캡슐화를 제공하는 것은 부주의한 코드로부터 버그가 야기되는 것을 제한해줍니다.
    - 캡슐화는 변수 범위에 대해서도 제공한다.
    - Scope은 전체 프로그램의 텍스트의 부분으로 정의됩니다. 식 또는 문에서 변수를 참조할 경우 메소드의 파라미터는 메소드의 body안으로 제한됩니다.
    - 지역 변수의 범위는 다음 중괄호 닫힘까지입니다.
    - 변수의 범위를 최대한 작게 유지하는 것은 프로그램에서 버그를 찾는것을 더 쉽게 해줍니다.
    - 다음과 같은 예시가 있습니다.
        
        ```java
        for (i = 0; i < 100; ++i) {
            ...
            doSomeThings();
            ...
        }
        ```
        
        - `i` 는 무한번 반복된다.
        
        ```java
        public static int i;
        ...
        for (i =0; i < 100; ++i) {
            ...
            doSomeThings();
            ...
        }
        ```
        
        - `i` 의 범위는 프로그램의 전체가 되었습니다. 어디서든지 참조 가능하고 루프는 100번 반복
        
        ```java
        public static int i;
        ...
        for (i =0; i < 100; ++i) {
            ...
            doSomeThings();
            ...
        }
        ```
        
        - 지역 변수를 활용해서 범위를 제한하였다.
        - `i` 는 문 내에서만 변경이 가능하다.
    - 최소화된 변수 범위의 활용 은 버그 지역화에 가장 강력합니다.
    - 항상 루프의 변수를 초기화 하세요.
        
        ```java
        int i;
        for (i = 0; i < 100; ++i) {
        ```
        
        - 지역화도 추가하세요.
        
        `for (int i = 0; i < 100; ++i) {`
        
    - `i` 를 루프 안에서만 한정시켜야 합니다.
        - 자바의 변수는 중괄호 안에서 선언해서 지역 변수로 만든다.
        - 하지만 함수를 시작할때 모든 변수를 선언하지 말아야한다 → 함수가 너무 커진다.
        - 전역 변수는 언제나 피할것… 필요할때 마다 코드를 파라미터로써 전달하는 방식이 좋다.
        - 전역변수는 언제나 재할당의 실수가 있음을 명시
    

## Summary

- avoid debugging
    - static typing을 활용해 버그 자체를 방지하다 자동화된 dynamic checking, 불변 타입 또한 활용
- keep bugs confined
    - fail fast를 assertion을 활용해서 도달한다.
    - 버그가 퍼지는 것을 막는다
    - 점진적 개발과 모듈화를 이용한다.
