## Code Review

- 본래 코드를 적은 사람이 아닌 다른 사람에게 코드를 보여주고 함께 하는것은 좋다.
- 코드 리뷰의 두가지 목적
    - **Improving the code** : 버그(예상되는)를 찾고 코드의 스타일을 서로 점검하면서 프로젝트의 기준의 일관성을 만든다.
    - **Improving the programmer** : 코드 리뷰는 프로그래머가 서로 배우고 학습하는데 좋은 방법, 또한 오픈소스 프로젝트에서 일어나는 다양한 사건에 대해 코드 리뷰는 좋은 대화 수단이 될 수 있다.
- 코드리뷰는 또한 산업현장에서 또한 동료 개발자들의 Code Review 없이는 push를 불가능하게 함

### Style Standards

- 대부분의 회사나 거대한 프로젝트에서는 코딩 스타일을 가진다 (*ex: 들여쓰기의 간격, 대괄호와 중괄호의 위치…*)
- JAVA 또한 일반적인 코드스타일이 있다
    
    ```jsx
    if (condition) {statements;
    }
    if (condition) {
    statements;
    } else {
    statements;
    }
    if (condition) {
    statements;
    } else if (condition) {
    statements;
    } else {
    statements;
    }
    ```
    
    더 자세한 기준은 https://www.oracle.com/java/technologies/javase/codeconventions-statements.html#15395 (업데이트 안됨)
    
- 하나의 프로젝트에 소속되어 있다면 그 소속의 코드 스타일을 따르는것은 중요하다.

### Smeely Example

```java
public static int dayOfYear(int month, int dayOfMonth, int year) {
    if (month == 2) {
        dayOfMonth += 31;
    } else if (month == 3) {
        dayOfMonth += 59;
    } else if (month == 4) {
        dayOfMonth += 90;
    } else if (month == 5) {
        dayOfMonth += 31 + 28 + 31 + 30;
    } else if (month == 6) {
        dayOfMonth += 31 + 28 + 31 + 30 + 31;
    } else if (month == 7) {
        dayOfMonth += 31 + 28 + 31 + 30 + 31 + 30;
    } else if (month == 8) {
        dayOfMonth += 31 + 28 + 31 + 30 + 31 + 30 + 31;
    } else if (month == 9) {
        dayOfMonth += 31 + 28 + 31 + 30 + 31 + 30 + 31 + 31;
    } else if (month == 10) {
        dayOfMonth += 31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30;
    } else if (month == 11) {
        dayOfMonth += 31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31;
    } else if (month == 12) {
        dayOfMonth += 31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31 + 31;
    }
    return dayOfMonth;
}
```

---

## Don’t Repeat Yourself

- 위 코드 처럼 복사/붙여넣기 하는 것을 매우 위험한 행위
    - 원본에 버그가 있는 상태에서 복사본 생성시 원본만 수정 할 경우 복사본의 버그는 남아 있음
- 위 코드에서 “4월” 은 총 8번 중복됨
- 만약.. 2월이 28일이 아니라 30일이라면..? 총 10줄의 코드를 실행해야함
- DRY 원칙을 준수하기 위해 조금 작은 단위로 코드를 나누는 습관을 가지고 그것을 호출에서 사용

---

## Comments Where Needed

- 좋은 프로그래머들은 코드와 Comments(주석)과 함께 한다.
- Comments는 코드를 이해하기 쉽게 하고 버그로 부터 더욱 더 안전하게 그리고 변화에 더 유연하게
- 좋은 Comment를 나타내기 위한 방법 중 하나는 메소드 또는 상위의 클래스의 동작을 문서화 하는 것이다.
- 자바에서는 Javadoc를 활용
- `@param` , `@return` 을 사용  아래 예시
    
    ```java
    /**
     * Compute the hailstone sequence.
     * See http://en.wikipedia.org/wiki/Collatz_conjecture#Statement_of_the_problem
     * @param n starting number of sequence; requires n > 0.
     * @return the hailstone sequence starting at n and ending with 1.
     * For example, hailstone(3)=[3,10,5,16,8,4,2,1].
     */
    public static List<Integer> hailstoneSequence(int n) {
    ...
    }
    ```
    
- 또다른 Comment의 용도는 출저 명시 및 코드를 어느 부분에서 복사하고 적용 했는지를 적는 것이다.
- 특정 라이센스는 제한적인 코드가 있기에 이러한 행동은 코드 저작권 문제를 방지한다.
- 또한 이런 문서화는 코드가 구식이 되는 것을 방지한다.
    - 예를들어 언제 스택 오버플로우 같은 곳에서 코드를 가져온지 적어놓는 것은 이 코드가 언제 작성된지 또한 확인 할수 있다.
- 하지만 코드를 직접 영어로 번역 하는것은 읽는 이가 JAVA를 이미 알고 있다고 있기에 좋지 않다.
    - `++i //increment i`  ← 어차피 자바는 모두가 안다.
- Comments로 코드의 의도를 설명하는 것보다는 코드로 의도를 표현하자
    - `if (a.flags &&.... b.age> 65)` 보다는 `if (a.isEligible()` 등을 통해 코드에서 설명
    - 단 라이브러리 사용시에 의도를 표현하기 모호한 경우 사용
- 해야 할 일이 있다면 TODO를 사용하는것도 좋은 방법

---

## Fail Fast

- 버그를 찾는것을 빠를 수록 좋다 → **Fail Fast**
- 빠른 문제의 관찰되어짐으로 빠르게 수정 가능 이런 방식은 Staic Checking이 Dynamic Checking보다 우월하다. 물론 잘못된 결과를 생성하는 것보단 Dynamic Chekcing이 더 빠르다.
    - Fail-fast에 적절한 방식 ( Static Checking > Dynamic Checking > Wrong answer )
- 위의 `DayOfYear` 코드는 fail fast라곤 볼 수 없다. 인자를 잘못 준다면 Wrong Answer가 나온다.
- 빠른 Fail Fast를 위해 static Checking과 Dynamic Checking을 사용
    - Static Checking 사용 → 파라미터 형식 강제
    
    ```java
    public enum Month { JANUARY, FEBRUARY, MARCH, ..., DECEMBER };
    public static int dayOfYear(Month month, int dayOfMonth, int year) {
      ...
    }
    ```
    
    - Dynamic Checking 사용 → 런타임 시에 예외 처리
    
    ```java
    public static int dayOfYear(int month, int dayOfMonth, int year) {
      if (month < 1 || month > 12) {
        throw new IllegalArgumentException(); //예외를 처리해서 프로그래머에게 알리는 것은 에러가 뒤로 숨는걸 막아준다.
      }
      ...
    } 
    ```
    
- **Iterator VS Enumeration**
    - Enumeration : 기존의 Iterator와 비슷하다. new 연산자로 생성이 안되며 Vector이용
    - Iterator : java Collections 저장 요소를 읽어오는 표준화 방법
    - 두 기능은 비슷하지만 결정적 차이가 존재 Enumeration는 fail-safe 방식을 채택하지만 Iterator는 fail- fast 방식
    - Fail-Safe : 부분적인 실패가 발생할시 바로 예외를 발생시키고 중단하는것이 아닌 계속 작동하고 안정적으로 종료되게 하는것
    - Enumeration은 순차적 접근 중 Collection 변경이 발생시 이를 무시하고 끝까지 동작
    - Iterator 같은 경우 Collecion 변경시 예외 발생

---

## Avoid Magic Numbers

- 대부분의 컴퓨터는 0과 1밖에 인지하지 못한다.
- 다른 숫자 또한 대부분 설명이 필요하다 (Comments라던가..)
- 차라리 명확한 이름 또는 상수로 대체하는게 더 좋아보인다.
    
    ```java
    if (password.length() > 7) {
        throw new InvalidArgumentException("password");
    }
    ```
    
    ```java
    public static final int MAX_PASSWORD_SIZE = 7; // Constatnt로 의미 부여
    
    if (password.length() > MAX_PASSWORD_SIZE) {
        throw new InvalidArgumentException("password");
    }
    ```
    
- `dayOfYear` 의 Magic Number를 피하는 방법
    - months 같은 경우 `FEBRUARY` , ...,`DECEMBER` 등으로 → 더 읽기 쉬움
    - days 같은 경우는 숫자는 유지한채 array나 list를 활용해서 `MONTH_LENGTH[month]` 를 사용
    - 59나 90 같은 프로그래머가 손으로 계산한 코드이다. → 이런 손으로 계산한 상수의 사용x `MONTH_LENGTH[JANUARY]` + `MONTH_LENGTH[FEBRUARY]` 를 사용
- Magic Numbers는 SFB(not safe form bugs), ETU(not easy to understand), RFC(not ready for change) 등을 유발
    - 일반적인 상수를 사용했더라도 의미 부여를 하지 않으면 위의 3개가 유발
    
    ```java
    final int FIVE = 5;
    final int THIRTY_SIX = 36;
    final int SEVENTY_TWO = 72;
    for (int i = 0; i < FIVE; ++i) {
      turtle.forward(THIRTY_SIX);
      turtle.turn(SEVENTY_TWO);
    }
    ```
    
    - 적절한 name을 통한 의미 부여
    
    ```java
    final double FULL_CIRCLE_DEGREES = 360.0;
    final int NUM_SIDES = 5;
    final int SIDE_LENGTH = 36;
    for (int i = 0; i < NUM_SIDES; ++i) {
      turtle.forward(SIDE_LENGTH);
      turtle.turn(FULL_CIRCLE_DEGREES / NUM_SIDES);
    }
    ```
    

---

## One purpose For Each Variable

- `dayOfYear` 는 다른 값을 사용하는데 재사용된다. (파라미터로 사용, 식의 계산, 리턴값)
- 매개변수와 변수를 재사용하는것을 지양, 적극적으로 변수를 새롭게 사용
- 특히 매개변수는 함부러 수정하지 않고 사용 ( final 키워드를 사용하는것도 좋음 )

---

## Use Good Names

- 변수나 메소드에 좋은 이름을 좋은 것은 가독성에 효과를 준다.
- JAVA의 명명 규칙은 아래와 같다.
    - 메소드의 이름은 CamelCase
    - 변수도 CamelCase
    - 상수는 모두 대문자
    - 클래스는 앞문자가 대문자
    - 메소드는 동사 구문, 변수와 클래스는 명사구

### CamelCase

- 낙타의 두개의 등과 비슷하다고 해서 명명된 방식
- *lowercamelCase*
    - *phoneNumber*
    - *createdAt*
- *UpperCamelCase (PascalCse)*
    - *PhoneNumber*
    - *CreateAt*

---

## Use Whitespace to Help the Reader

- 일관된 공백의 사용
- Tab은 각각의 IDE 같은 환경마다 기준이 다르다. → 다른 환경에서 사용시 들여쓰기가 망가짐.
- Space를 활용한 공백 문자의 사용을 지향

---

### Smelly Examlpe(2)

```java
public static int LONG_WORD_LENGTH = 5;
public static String longestWord;

public static void countLongWords(List<String> words) {
   int n = 0;
   longestWord = "";
   for (String word: words) {
       if (word.length() > LONG_WORD_LENGTH) ++n;
       if (word.length() > longestWord.length()) longestWord = word;
   }
   System.out.println(n);
}
```

---

## Dont’use Global Variables

- 전역 변수: 프로그램 어디서나 접근이 가능한 변수 → 사용 자제
    - 병행성 문제
    - 긴 생명 주기 → 리소스 소비량이 늘어남
    - 암묵적 결합 → 각 모듈간의 응집도를 떨어트림
- `public` , `static` 이 자바의 전역 변수
- 전역변수를 사용하기 보단 return values를 사용 또는 메소드를 호출하는 객체를 사용
- 변수를 상수( `final` )로 사용하는 것은 전역변수의 위험을 일부 피할 수 있다.
- 위의 code에서는`LONG_WORD_LENGTH` , `word` 가 상수화 가능
    - 변수가 재할당 되지 않음 → 즉 상수화 가능
- 반복문 내에서 재 할당이 이뤄지는 `longestWord` 등은 상수화 불가능

---

## Method Should Return Results, not Print Them

- 위 코드는 결과의 일부를 콘솔로 직접 전달한다. 즉 변화에 대해 약한 RFC이다.
- 만약 다른 문맥이나 숫자에 대해 계산할려고 하면 코드를 재작성 해야한다.
- 프로그램은 가장 높은 수준만 사람과 상호작용하는게 원칙적
    - 단 디버깅 output은 예외

---

## Summary

- 코드를 붙여 넣지 말것 (Don’t Repeat Yourself)
- 필요한 부분에 적절한 Comments
- 빠른 버그 찾기 (Fall fast)
- Magic Number 피하기
- 변수에는 하나의 목적만 수행하기
- 이름 잘 짓기
- 전역변수 사용 지양
- 메소드는 결과만 반환, 출력은 사용안함
- 적절한 공백의 사용
