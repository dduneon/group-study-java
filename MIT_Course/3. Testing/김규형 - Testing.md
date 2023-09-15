### 이번 장의 목표

- [x]  Test-first programmin의 절차를 알고, 테스팅 값을 이해하는것
- [x]  메소드의 input과 output에 적절한 테스트를 디자인 하고 좋은 TC를 고르는것
- [x]  테스트가 코드에 어느 범위 까지 적용 되는것을 이해하는것
- [x]  블랙박스와 화이트박스 테스트를 이해하고 유닛과 통합 테스트 그리고 자동 회귀 테스트를 이해한다.

## 검증

- 일반적으로 테스팅의 대부분 절차는 검증이라 불린다.
- 검증의 목표는 야기 될 수 있는 프로그램의 잠재적 문제를 찾아 정확성을 높이는 것.
- 검증은 아래를 포함한다.
    - **Foraml reasoning(형식추론)** : 공식 증명으로 프로그램을 검증한다. 손으로 하는것보다 tool을 주로 사용, 주로 운영체제 스케줄러, 바이트코드 인터프리터등 작은 부분에 사용된다.
    - **Code review** : 누군가가 코드를 읽고 이것을 추론한다. 버그를 방지하는데 좋은 방법이다
    - **Testing** : TC를 input에 결과를 확인한다.
    
    ---
    

## 테스팅 하는것이 어려운 이유

- **Exhaustive Testing(철저한 테스팅)** : 전체를 테스트하기에는 프로그램이 너무 거대함 (ex: 32비트의 부동 소수점 계산    a*b 는 2^64개의 TC가 존재
- **Haphazard Testing(우연적 테스팅)** : 단지 실행하는지 확인만 하는것은 버기를 잡기엔 좋지않다. 왜냐하면 임의의 TC가 버그를 잡지 못할 수도 있다.
- **Random or statistical Testing(무작위 또는 정적 테스팅)** : SW에서는 잘 어울리지 않음, 일반적인 다른 공학에서는 주로 사용되지만… SW는 대부분 불연속적, 특정한 공간에 이산적으로 발생 (ex: 스택 오버플로우, 정수 값 초과…) 그렇기에 TC를 선별할때 잘 선별 해야한다.

---

## Test-first Programming

- 테스트는 언제나 빠르게 자주 그렇지 않으면 검증되지 않은 코드가 쌓여 티버거 시간과 고통이 늘어난다.
- test-first-programmin은 코드를 쓰기도 전에 테스트를 하는것이다.
    - 함수의 기능을 작성한다.
    - 구체적으로 어떻게 테스트 할지를 작성한다.
    - 실제 코드를 작성한다
- **specification(명세)** 에 함수의 input(paprameter와 같은…)과  output을 묘사, 그리고 input에 따른 return값이 어떻게 관련 있는지 또한 묘사
- **specification** 또한 버그가 없도록 테스트가 필요

---

## Choosing Test Cases by Partitioning

- 좋은 테스트 케이스를 만드는것은 프로그램 디자인에 중요한 요소
- 이것을 위해 **subdomains** 활용
    - input의 집합
    - 각 subdomain은 그 구역의 모든 범위에 적용됨
    - 그렇기에 각각의 subdomain에서 하나 골라서 테스트
- 이러한 생각 뒤에는 한 구역의 input space에는 비슷한 결과를 낼 것이라고 생각
- 이러한 접근 법은 한정된 테스트 자원이 있을때 사용

### Example : `**BigInteger.multiply()**`

- 모든 정수를 나타난다. `int` 와 `long` 은 제한적인 정수
- `multiply()` 는 곱셈 기능
- 이렇게 사용한다.
    
    ```jsx
    BigInteger a = ...;
    BigInteger b = ...;
    BigInteger ab = a.multiply(b);
    ```
    
- 이 코드는 다음과 같은 input 쌍이 존재 할 수 있다.
    - a와 b가 양수
    - a와 b가 음수
    - a는 양수, b는 음수
    - a는 음수, b는 양수
    - a나 b가 1,0,-1 일 경우
- 계산 방식 또한 다음과 같다.
    - 크기에 대해 값이 작을 경우 효율을 위해 `int` 와 `long` 으로 계산
    - `Long.MAX_VALUE` 등 2^63 과 같은 극적으로 큰 값일때
- 이러한 점을 고려했을때 우리는 다음과 같은 test suite를 만들 수 있다.
    - (-3, 25) → a와 b가 작고 음수와 양수
    - (0, 30) → a는 0, b는 작은 양수
    - (2^100, 1) → a는 큰 양수 b는 1
    - 등등…

### ****Include Boundaries in the Partition****

- 버그는 언제나 boundaries와 subdomains에서 일어난다
    - ‘0’은 양수와 음수의 경계
    - `int` 와 `double` 같은 경우 각 최대 최소
    - 비어있는 String, list등의 Collection Type
    - 요소의 첫번째 또는 마지막
- 왜 버그는 경계에서 일어날까?→ 프로그래머들은 **off-by-one mistakes** (한가지 실수)를 저지른다.
    - `<=` 를 `<` 로 쓰는것과 같은…
- 다른 문제로는 몇몇 경계들은 특별한 case로 다뤄야하는 경우도 있을 수 있다.
- 아니면 경계에서는 코드가 지속적으로 진행되지 않을 수도 있다.
    - `int` 같은 경우 최대 값을 넘어가면 마이너스의 값을 가진다.
- `max : int x int -> int`
    - a와 b의 relationship
        - a < b
        - a = b
        - a > b
    - a의 값은
        - a = 0
        - a < 0
        - a > 0
        - a = int의 최대 값
        - a = int의 최소 값
    - b의 값 또한 위와 같음
- 이러한 정보를 토대로 아래와 같은 테스트 값을 산출
    - (1, 2) → a < b, a > 0, b >0
    - (0, 0) → a = b, a= 0, b= 0
    - (Integer.MAX_VALUE, Integer.MIN_VALUE) → a > b, a = maxInt, b = minInt

### Two Extemes for Covering the Partition

- input space 의 분리가 나누어진 후에 얼마나 많은 test를 준비 할 것인가
- **Full Cartesian Product**
    - 각각의 분리에 대해 3 x 5 x 5 = 75 > 는 7 x 7 = 49보다 크다
    - 물론 a < b , a= 0, b= 0 같은 경우는 이렇게 해당 될수 없다.
- **Cover each Part**
    - 각각의 텍스트는 최소 하나 이상의 범위에 적용된다. 하지만 이것을 모두 조합 할 필요는 없고 위의 `max` 같은 경우는 최소 5개로 적용이가능하다.

---

## Blackbox and Whitebox Test

- 위에서 언급한 *specification*을 회상
- **Blackbox testing :**  함수의 기능을 생각하지 않고 specification(명세)만을 보고 TC를 정함
- **Whitebox testing** : 함수가 실제로 어떻게 돌아가는지를 생각하면서 TC를 만듬 위의 `max` 와 `multiply` 가 대표적
    - whitebox testing을 할때에는 예외에 대해 명시 (exception에 대한 throws)의 경우 굳이 NullPointEception에 대한 테스트를 할 필요가 없다. ..?

---

**Exercises**

```jsx
/**
 * Sort a list of integers in nondecreasing order.  Modifies the list so that 
 * values.get(i) <= values.get(i+1) for all 0<=i<values.length()-1
 */
public static void sort(List<Integer> values) {
    // choose a good algorithm for the size of the list
    if (values.length() < 10) {
        radixSort(values);
    } else if (values.length() < 1000*1000*1000) {
        quickSort(values);
    } else {
        mergeSort(values);
    }
}
```

- 이 코드에 대해서 화이트 박스 경계값 테스트는 `values = [9, 8, 7, 6, 5, 4, 3, 2, 1, 0]` 이다.
- `values = []` 는 white box Test용 값은 아니다.

---

## Coverage

- 하나의 TC가 얼마나 코드의 범위를 체크 할수 있는가 (화이트 박스 테스트)의 지표
- **Statement coverage :** TC가 Statement 문을 전부 테스트 하는가?
- **Branch Coverage :** 모든 `if` 와 `while`을 테스트 하는가?
- **Path Coverage :** 모든 조합 가능한 조건문들을 테스트 하는가? = 모든 경로를 테스트 하는가?
- 일반적으로 patch > branch > statment 순으로 강력하다
- 하지만 현실적으로 Path나 Branch Coverage를 이뤄낼순 없다
- 현실적으론 하나의 테스트 케이스가 모든 Statement에 도달 할수 있게 조절 → 도구를 활용

---

## Unit Testing and Stubs

- 테스트를 할때는 가능하다면 모듈단위로 고립적 테스트 하는것이 좋다.
- 또한 고립된 모듈은 디버깅 하기도 수월 → Unit Test의 필요성
- 이와 반대로 모듈을 조합하거나 또는 전체 프로그램을 테스트 하는것을 **intergation test**라고 불린다.
    - 이런 intergation test는 버그를 찾는것에 소모되는 시간이 크지만 여전히 전체 모듈 사이의 연결을 테스트하는데 여전히 중요하다.
- 하지만 철저한 Unit test는 intergation Test에 역할을 어느정도 대체해 버그를 찾는데 들이는 비용을 감소 시킬수가 있다.
    
    ```jsx
    /** @return the contents of the web page downloaded from url 
     */
    public static String getWebPage(URL url) {...}
    
    /** @return the words in string s, in the order they appear, 
                where a word is a contiguous sequence of 
                non-whitespace and non-punctuation characters 
     */
    public static List<String> extractWords(String s) { ... }
    ```
    
    ```jsx
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
    
- 우리는 위 코드에서 3개의 단위 테스트를 할 수 있다.
    - `getWebPage()` : 다양한 url 을 가져온다.
    - `extractWords()` : 다양한 String을 테스트한다.
    - `makeIndex()` : 다양한 url의 집합을 테스트 한다.
- `extrackWords()` 을 단위 테스트 한다면?
    - `getWebPage()` 에 의존해서는 안된다.
    - 직접적으로 String을 받아오는 방식으로 해야한다. 왜? 다른 모듈에 버그가 있을수도 있기 때문에.
- `makeIndex()` 를 단위 테스트 한다면?
    - 쉽게 독립되긴 힘들어 보인다.
    - 연관된 다른 메소드들의 단위 테스트를 실행해 신뢰감을 높인다면 해당 메소드도 테스트가 가능
    - 이런식으로 테스트 완료된 구조를 넓혀간다.
    - 만약 어떤식으로도 테스트 하고 싶다면 **stub**을 사용할 수 있을것이다.

---

## Automated Testing and Regression Testing

- **Automated testing** 은 자동적으로 실행과 테스트를 체크한다.
- **test driver** 는input에 대한 결과를 체크 하기 위해 자동적으로 프로그램과 상호작용하지 않습니다. 대신에 자동으로 테스트 케이스가 맞는지는 체크합니다.
- 이러한 테스트 도구로는 JUnit이 주로도움을 줍니다.
- 현재로썬 테스트 도구는 연구 분야 역시 가장 좋은건 스스로 해보는것.
- 수정된 코드를 재 실행하는 것은 중요하다. 그리고 기능을 추가하거나 버그를 수정할때 마다 새로운 테스트를 자동으로 실행하는 것을 **regression testing**이라고 한다.
- 버그가 발생하고 그 코드를 수정한다면 다음 테스트 코드에는 그 버그를 유발 시켰던 TC를 추가하고 다시 테스트한다 이를 또한 regression testing이라고도 불린다.
- 이러한 아이디어는 또한 **test-first-debugging** 이라고도 불리는데 버그가 증가할때 즉시 TC에 추가한다. 버그를 수정한다면 모든 테스트 케이스들은 여전히 통과 될 것이다.
- Automated Testing and Regression Testing 은 모두 서로 조합되서 사용된다.  Regression Testing 은 테스트가 자동으로 자주 실행 될때 실용적이며 이미 프로젝트가 완료 되어다면 회귀 테스트로 전체 자동 테스트를 하는것이 합리적이다.

---

## Summary

- Test-first programming은 코드를 쓰기전에 테스트 코드를 적는것
- 분할과 경계는 테스트 케이스를 체계적으로 생성할 수 있다.
- 화이트 박스 테스트와 Statement Coverage는 test suit(test case를 묶은것)를 채우도록 해준다
- 각 모듈의 단위 테스트는 가능한한 고립시켜서 진행한다.
- 자동화된 테스팅은 버그를 방지한다.
