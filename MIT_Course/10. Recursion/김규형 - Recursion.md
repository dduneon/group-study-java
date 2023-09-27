# Recursion(23.09.27)

### Object

- recursion 문제를 base case와 recursive steps로 분리 할 수 있습니다.
- recursion에 help methods를 이해합니다.
- recursion vs iteation 의 장단점을 이해합니다.

## Recursion

- 이미 recursion에 대한 사양을 가지고 있을때, 어떻게 구현하면 좋은가?
- recursion은 모든 프로그램에 적절하다고는 볼 수 없지만 개발 도구 중 하나로는 여전히 중요한 방식입니다.
- 일반적으로 피보나치 함수와 나누기에 대해서 재귀를 사용한다.
- 재귀 함수는 base case와 recursive steps로 나누어진다.
    - **base case** : 함수 호출에 대해 즉시 결과를 계산한다.
    - **recursive steop** : 하나 이상의 재귀적 호출에 도움을 받아 계산
- 팩토리얼에 대해서 아래 두가지 예시
    - Iterative
    
    ```java
    public static long factorial(int n) {
      long fact = 1;
      for (int i = 1; i <= n; i++) {
        fact = fact * i;
      }
      return fact;
    }
    ```
    
    - Recursive
    
    ```java
    public static long factorial(int n) {
      if (n == 0) {
        return 1;
      } else {
        return n * factorial(n-1);
      }
    }
    ```
    
    - recursive 구현에서 n = 0 일때 즉시 값을 반환한다. → Base case
    - n > 0 보다 클 때 재귀를 호출 → Recursice steps
    - 이를 시각화
    
    | starts in
    main | calls
    factorial(3) | calls
    factorial(2) | calls
    factorial(1) | calls
    factorial(0) | returns to
    factorial(1) | returns to
    factorial(2) | returns to
    factorial(3) | returns to
    main |
    | --- | --- | --- | --- | --- | --- | --- | --- | --- |

- 자기자신인 `factorial(n- 1)` 을 호출하면서 call Stack을 쌓이게 하고 그 후에 풀어가면서 계산하는 방식
- 다른 예 피보나치
    
    ```java
    /**
     * @param n >= 0
     * @return the nth Fibonacci number
     */
    public static int fibonacci(int n) {
        if (n == 0 || n == 1) {
            return 1; // base cases
        } else {
            return fibonacci(n-1) + fibonacci(n-2); // recursive step
        }
    }
    ```
    
- 피보 나치는 n= 0 과 n = 1의 base case를 가지고 있다.

## ****Choosing the Right Decomposition for a Problem****

- 재귀를 위해 문제를 분해하는 것은 중요하다.
- 좋은 decompositions(분해)는 짧고, 이해하기 쉽고, 버그에 안전하고, 변화에 유연해야한다.
- 다음과 같은 명세를 가정하자
    
    ```java
    /**
     * @param word consisting only of letters A-Z or a-z
     * @return all subsequences of word, separated by commas,
     * where a subsequence is a string of letters found in word
     * in the same order that they appear in word.
     */
    public static String subsequences(String word)
    ```
    
- `subsequences("abc")` 의 경우 `"abc,ab,bc,ac,a,b,c,”` 분해 한다면?
- 다음과 같은 재귀 코드를 작성할 수 있다.
    
    ```java
     1 public static String subsequences(String word) {
     2     if (word.isEmpty()) {
     3         return ""; // base case
     4     } else {
     5         char firstLetter = word.charAt(0);
     6         String restOfWord = word.substring(1);
     7
     8         String subsequencesOfRest = subsequences(restOfWord);
     9
    10         String result = "";
    11         for (String subsequence : subsequencesOfRest.split(",", -1)) {
    12             result += "," + subsequence;
    13             result += "," + firstLetter + subsequence;
    14         }
    15         result = result.substring(1); // remove extra leading comma
    16         return result;
    17     }
    18 }
    ```
    

## ****Structure of Recursive Implementations****

- **Base case** : 더 이상 분해 할 수 없는 경우 빈 문자열, 빈 리스트, 빈 트리, 0 등등…
- **recursive step** : 분해할 수 있는 것 작게 분해 후 재결합하여 문제를 해결하는 단계
- 문제를 최대한 작게 분할 하는 것은 중요하다 이렇게 하지 않으면 재귀가 끊나지 않을 가능성도 존재

## Helpe Methods

- `subsequences()` 는 문제를 decomposition 하고 재귀적으로 해결하는 예시이다.
- 이러한 subproblem을 만들어서 문제를 해결하는 방식은 direct recursive implenation 이다.
- 어떠한 케이스의 경우 명세에서 recursive step에 대한 강력한 명세를 표현하는 것은 재귀적 분해를 더 우아하게 도와준다.
- 이러한 경우 부분적인 subsequence를 활용하여 부분적인 완성을 한다면?
- 예를 들어 “orange”가 있다면 첫 글짜 “o” 를 받고 “range” 는 재귀적으로 확장을 시도한다.
- 이를 코드로 작성한다면…
    
    ```java
    /**
     * Return all subsequences of word (as defined above) separated by commas,
     * with partialSubsequence prepended to each one.
     */
    private static String subsequencesAfter(String partialSubsequence, String word) {
        if (word.isEmpty()) {
            // base case
            return partialSubsequence;
        } else {
            // recursive step
            return subsequencesAfter(partialSubsequence, word.substring(1))
                 + ","
                 + subsequencesAfter(partialSubsequence + word.charAt(0), word.substring(1));
        }
    }
    ```
    
- 이러한 `subsequencesAfter` 를 **helper method** 라고 부른다.
- 이것은 본래 `subsequences` 와는 다른 명세를 만족시킵니다. 새로운 파라미터를 가지고 있기 때문에 (`partialSubsequence`)
- 이 매개변수는 단순한 반복에서 사용하는 지역 변수와 비슷한 역할을 한다.
- 이것은 단어의 끝까지 도달하는 것을 부분 연속을 유일한 결과로 반환한다.
- 이러한 재귀는 가능한 시퀸스를 백트랙킹한다.
- 구현을 완료하기 위해, 원본을 구현한다.
    
    ```java
    public static String subsequences(String word) {
        return subsequencesAfter("", word);
    }
    ```
    
- **helper method** 를 사용자에게 노출시키지 말것.
- 이런 helper method를 명세를 변경할 필요는 없다. → 이는 변경성을 저하
- 사용자는 언제나 기존의 방식을 유지한것처럼 보이게 하자.

## ****Choosing the Right Recursive Subproblem****

- 다음과 같은 예제 integer 를 string (2진법)으로 변환하는 예제
    
    ```java
    **
     * @param n integer to convert to string
     * @param base base for the representation. Requires 2<=base<=10.
     * @return n represented as a string of digits in the specified base, with
     *           a minus sign if n<0.
     */
    public static String stringValue(int n, int base)
    ```
    
- `stringValue(16, 10)` 는 `10000` 를 리턴한다.
- 음수를 처리할려면 이 코드를 추가한다.
    
    `if (n < 0) return "-" + stringValue(-n, base);` 
    
- n = 829 ( 10진수 ) 라고 가정할때 어떤식으로 재귀로 분리 하겠는가?

## ****Recursive Problems vs. Recursive Data****

- 우리가 봐왔던 케이스의 경우는 문재 구조가 자연스럽게 재귀적인 겅의를 부여하는 경우
- 재귀적 문제는 도구 상자에서 재귀적 해결을 꺼내는 것과 같다.
- 만약에 데이터 자체가 재귀적인 경우에는?
    - 대표적으로 파일 시스템 A 폴더 → B 폴더 → C파일
- `java.io.File` 은 대표적으로 파일 사용에 사용되는 라이브러리
- `f.getParentFile()` 은 f 파일의 상위 폴더를 반환하는 메소드
- 이런 재귀적 데이터의 경우 자연스럽게 재귀 구조를 갖는다.
    
    ```java
    /**
     * @param f a file in the filesystem
     * @return the full pathname of f from the root of the filesystem
     */
    public static String fullPathname(File f) {
        if (f.getParentFile() == null) {
            // base case: f is at the root of the filesystem
            return f.getName();
        } else {
            // recursive step
            return fullPathname(f.getParentFile()) + "/" + f.getName();
        }
    }
    ```
    

## Reentrant Code

- **재진입** 이라는 용어는 일반적인 프로그래밍에서 특별한 경우에 속한다.
- 재진입 코드는 안전하게 재진입 하기에 호출되는 와중에도 재 진입이 가능하다.
- 재진입 코드는 파라미터와 지역변수의 상태를 전체적으로 안전하게 지켜준다.
- 전역변수의 사용을 없애고 가변 객체의 aliasese와 공유하지 않는다.
- Direct recursion은 재진입이 일어날 수 있는 한가지 방법으로, `factorial()` 메소드는 `factorial(n)` 의 작업이 끝나지 않았지만 `factorial(n-1)` 을 호출 하는 방법으로 디자인 되어졌습니다.
- Mutal recursion은 두개 또는 더 많은 경우가 일어날 수 있다.
    - A가 B를 호출하고 다시 A를 호출한다.
    - 이러한 방식은 항상 프로그래머가 의도적으로 설계 하는 방식이지만 버그를 초래할 수도 있다.
- Concurrency에서도 일어날 수 있다.
- 재진입 코드는 언제나 안전하게 사용해야함을 잊지 말자

## ****When to Use Recursion Rather Than Iteration****

- 재귀를 사용하는 두 유형에 대해 공부했다
    - 문제 자체가 자연스럽게 재귀일때
    - 데이터가 재귀일때
- 재귀를 사용하는 또 다른 이유는 불변성입니다.
- 이상적인 재귀 구현에 있어 모든 변수는 상수 이고 불변성을 가져야합니다.
- 재귀 메소드는 어떠한 것도 변형하지 않는다는 점에서 순수한 메소드입니다.
- 메소드를 파라미터와 리턴 값에 대한 관계로 이해할 수 있다면 side effects는 없다고 볼 수 있다.
- 다양한 함수적 프로그래밍에서 이러한 이점을 가지며 명령형 프로그래밍 보다 이해하기가 쉽다.
- 하지만 재귀 구조에서는 많은 임시 변수가 선언되기에 이러한 동작을 스냅샷을 그리기에는 조금 복잡하다
- 또한 반복 솔류션 보다 공간 복잡도가 높다는 것인데 call Stack을 호출하한다는 단점이 있다.

## ****Common Mistakes in Recursive Implementations****

- 재귀에서 일어날 수 있는 실수
    1. Base case의 누락
    2. 더이상 밑의 step으로 감소되지 못함 → 무한 재귀

## Summary

- 오늘 공부한 내용
    - 재귀 문제와 재귀 데이터
    - 재귀 문제의 대안적 해결
    - Helper method
    - 반복 vs 재귀
- 재귀는 불변 객체를 사용하기에 버그로부터 안전해진다
- 재귀는 반복 솔루션보다 짧기에 이해가기가 쉽다.
- 재귀는 변화에 유연하다.
