# Code Review

## Code Review

→ 코드 리뷰는 코드 개발자가 아닌 다른 사람들이 코드를 신중하고 체계적으로 연구하는 것이다.

**코드 리뷰의 두가지 목적**

- **Improving the code(코드 개선):** 버그 찾기, 나타날 수 있는 버그 예상, 코드 명확성 확인, 프로젝트의 스타일 표준과 맞는지 확인
- ****Improving the programmer.(프로그래머 개선):**** 코드 리뷰는 프로그래머가 새로운 언어의 기능, 프로젝트 설계, 프로젝트 코딩 표준, 그리고 새로운 기술등에 대해 서로 배우고 가르치는 중요한 방법이다. 오픈 소스 프로젝트에서는 코드 검토라는 맥락에서 많은 대화가 이루어진다.

************************************코드 리뷰는 업계에서도 널리 시행된다. Ex) 구글 같은 곳에서는 다른 개발자가 코드 검토를 승인할 때까지 repository에 Push 할 수 없다.************************************

### Style Standards

**→ 대부분의 회사나 큰 프로젝트는 `coding style standards` 를 가진다. coding style standard를 가짐으로서 공백을 지정하고 괄호나 중괄호를 어디에 넣어야하는지까지 매우 자세하게 설명할 수 있다.**

**이는 결국 취향과 스타일의 문제임**

**자바에도 스타일 가이드가 있다.**

- 여는 중괄호는 명령문이 시작하는 줄 끝에 있어야한다. 닫는 중괄호는 시작 부분과 같이 인덴트가 맞춰있어야 함

**→ 팀프로젝트를 위해서는 팀원들과 프로젝트 규약을 맞춰 코드 스타일을 맞추는게 낫다.**

---

## Smelly Example #1

→ 개발자들은 종종 안좋은 코드를 제거해야 할 `bad smell` 이라고 묘사한다.

**안좋은 코드 예시**

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

## Don’t Repeat Yourself(DRY)

→ **중복된 코드는 안전하지 않다.**

**→ 만약 두가지 장소에 유사한 코드가 있을 때 한 코드에서 오류가 발생한다면 다른 코드는 고치지 않는 경우가 있다.**

**코드를 복사하는 방법은 단편적으로는 편하지만 복사하는 코드가 길수록 위험이 커진다.**

<aside>
💡 **같은 행동을 반복하지마라**

</aside>

---

## Comments Where Needed

**→ 좋은 소프트웨어 개발자는 코드에 주석을 작성하여 코드를 더 쉽게 이해하고 버그로부터 안전하게(중요한 조건을 문서화하면서) 변경 준비가 되어야 한다.**

`**specification` 은 메소드나 클래스 위에 나타내며 동작을 문서화하는 가장 중요한  `Comment`중 하나이다.**

- **자바에서는 일반적으로 `Javadoc` 주석으로 작성된다. ( /** 로 시작하고 @param @ return 등 @-syntax을 사용한다.)**

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

**다른 중요한 코멘트로는 다른 곳에서 가져오거나 수정한 코드는 출처를 명시해주어야 한다.**

```java
// read a web page into a string
// see http://stackoverflow.com/questions/4328711/read-url-to-string-in-few-lines-of-java-code
String mitHomepage = new Scanner(new URL("http://www.mit.edu").openStream(), "UTF-8").useDelimiter("\\A").next();
```

**→ 출처를 명시하는 이유는 저작권 침해를 피하기 위해서**

************************************몇몇 불필요한 코멘트는 하지 않아도 된다. ex) 코드를 직접 설명하는 것은 독자가 최소한 Java를 알고 있다고 가정해야 한다.************************************

```java
while (n != 1) { // test whether n is 1   (don't write comments like this!)
   ++i; // increment i
   l.add(n); // add n to l
}
```

**→ 그러나 무슨 역할을 하는지 애매한 코드에는 주석이 있어야 한다.**

```java
sendMessage("as you wish"); // this basically says "I love you"
```

---

## Fail Fast

**→ 빠르게 실패한다는 것은 빠르게 코드에서 버그를 빠르게 드러낸다는 것을 의미한다. 문제를 빠르게 발견할수록 해결하기 쉬움**

**→ 첫번째 단원에서 공부했듯이 `static checking`에서 발견된 오류는 `dynamic checking`보다 빠르게 발견되고 `dynamic checking` 는 잘못된 답을 생성하는 것 보다 더 빨리 오류가 발견된다.**

---

## Avoid Magic Numbers

**→ 컴퓨터 과학자들이 실제로 유효한 값으로 인식하는건 0,1 두가지뿐이다( 3가지라는데 이유가 뭐임 (?))**

**상수에 대한 설명이 필요하다. 주석을 사용하는 것도 있지만 설명하기 `좋은 이름`으로 선언하는게 좋다.**

**위에 예제를 예시로 `dayOfYear`이 있다.**

- **2, …, 12개월은`FEBRUARY`... `DECEMBER` 로 쓰는게 이해하기 훨씬 쉽다.**
- **30, 31, 28일등은 배열이나 리스트 맵(ex. MONTH_LENGTH[month])등으로 되어있으면 훨씬 이해하기 쉽고 중복된 코드를 제거 할 수 있다.**
- **위 예제에서 59와 90은 `Magic Number`의 안좋은 예이다. 주석이 없고 문서화되지 않았을 뿐만 아니라 프로그래머가 직접 계산한 결과이다. → 그러나 직접 계산한 상수를 하드코딩하면 안된다. 59가 아니라 `31 + 28` 와 같이 출처를 명확하게 해줘야한다.**
- **정확하게는 `MONTH_LENGTH[JANUARY]` + `MINTH_LENGTH[FEBRUARY]` 가 더 명확하다.**

---

## One Purpose For Each Variable

**→ 예제에서 `dayOfYear`에 파라미터 `dayOfMonth` 는 날짜가 아닌 리턴 값으로 재사용된다.**

**파라미터와 변수는 재사용하지 않는게 좋다. 변수는 프로그래밍에서 부족한 자원이 아니다.** 

**자유롭게 사용해도 된다. 만약 한가지를 의미하던 변수가 밑에서 다른 것을 의미한다면 읽는 사람에게 혼란을 줄 수 있다.**

**특히 메소드 파라미터는 원래 값을 알고 싶어 할 수 있으므로 일반적으로 수정되지 않은 상태로 두는게 좋다.  이를 위해서 `final` 키워드를 통해서 메서드 파라미터 및 변수등에게 사용하는 것이 좋다. Static checking을 해주기 때문에 좋음**

```java
public static int dayOfYear(final int month, final int dayOfMonth, final int year) { ... }
```

---

## Smelly Example #2

**→ `dayOfYear` 코드에는 윤년을 처리해주지 않아서 잠재적인 버그가 있다. 이를 해결하기 위해 메서드를 작성**

```java
public static boolean leap(int y) {
    String tmp = String.valueOf(y);
    if (tmp.charAt(2) == '1' || tmp.charAt(2) == '3' || tmp.charAt(2) == 5 || tmp.charAt(2) == '7' || tmp.charAt(2) == '9') {
        if (tmp.charAt(3)=='2'||tmp.charAt(3)=='6') return true; /*R1*/
        else
            return false; /*R2*/
    }else{
        if (tmp.charAt(2) == '0' && tmp.charAt(3) == '0') {
            return false; /*R3*/
        }
        if (tmp.charAt(3)=='0'||tmp.charAt(3)=='4'||tmp.charAt(3)=='8')return true; /*R4*/
    }
    return false; /*R5*/
}
```

---

## Use Good Names

**→ 좋은 메서드와 변수의 이름은 길고 설명적이여야한다. 이름을 잘 지어서 코드 그 자체를 읽기쉽도록 만들면 Comment를 달아야하는 것을 피할 수 있다.**

예를 들어

```java
int tmp = 86400;  // tmp is the number of seconds in a day (don't do this!)
// -> tmp는 하루의 초수 이다.
int secondsPerDay = 86400;
// 이렇게 이름으로 표현하는 것이 좋음
```

**→ 일반적으로 `tmp`, `temp`,`data` 와 같은 변수 이름은 프로그래머의 게으름 증상이다.**

**(모든 변수는 임시적이고 모든 변수는 데이터이기 때문에 의미가 없어지기 때문에)**

**그러므로 코드 자체를 명확하게 일긍ㄹ 수 있도록 설명이 담긴 이름으로 해야한다.**

**Python의 클래스는 일반적으로 클래스는 대문자, 변수는 소문자, 단어는 _로_분리한다. 자바에서는**

- methodNamedWithCamelCase
- variableAreAlsoCamelCase
- CONSTANT_ARE_IN_ALL_CAPS_WITH_UNDERSCORES
- ClassesAreCapitalized
- package.are.lower.case.and.separate.by.dot

`**getDate` or `isUpperCase` 같은 메서드는 일반적으로 동사구인데 변수 및 클래스 이름은 일반적으로 명사로 적는다. → `wd` 보다는 `word`가 좋고 `msg` 보다는 `message` 가 좋다.**

---

## Use Whiterspace to Help the Reader

**→ 일관된 들여쓰기를 사용해야한다. 위의 예제 중 `leap` 예는 좋지 않고 예제인 `dayOfYear`이 훨씬 좋다.**

**→ 들여쓰기를 하는데 있어서는 탭이 아닌 공백을 사용해야한다. 문자를 넣어주어야한다. 그 이유로는 프로그래머가 사용하는 도구마다 탭 문자를 다르게 처리한다. 공백이 2개일수도 4개일수도 8개일수도 있다.** 

**→ 명령줄에서 `git diff` 를 실행하거나 소스 코드를 다른 편집기에서 보는 경우 들여쓰기가 엉망일 수 있으므로 공백을 이용하는 것이 낫다. Tab을 누를 때 공백 문자를 삽입하도록 프로그래밍 편집기를 설정하는 것이 좋다.**

---

## ****Smelly Example #3****

→ 이 예시가 안 좋은 코드 예시의 세 번째다.

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

## ****Don’t Use Global Variables****

→ 전역 변수를 사용하는 것을 피해야 한다.

- **변수(variable) : 이름이나 의미가 바뀔  수 있다.**
- **전역(global)** : 프로그램 어디서든지 접근 가능하고 변경 가능하다.

******************************************************************************************자바에서는 전역 변수를 `global static` 로 선언한다. `public` modifier은 어디서든지 접근 할 수 있게 만들어주고 `static`는 단일 인스턴스가 있음을 의미한다.**

---

## ****Methods Should Return Results, not Print Them****

**→ `countLongWords`메서드는 변화할 준비가 되어 있지 않다. 리턴 타입이 void이기 때문에 출력이 아닌 계산과 같은 목적으로 숫자가 필요한 상황에서 사용하기 위해서는 다시 작성해야한다.**

**→ 일반적으로 프로그램의 가장 높은 수준의 부분이 사용자와 콘솔로 상호작용해야하고 낮은 수준은 파라미터값을 입력받아 결과값 반환해준다. 출력은 설계의 일부가 되어서는 안된다.**

---

## Summary

**코드리뷰는 소프트웨어의 품질을 향상 시키기 위해서 널리 사용되는 기술이다.**

**좋은 코드를 위해서 지켜야 할 원칙**

- **DRY원칙(반복하지마라)**
- **필요한 곳에 Comment**
- **Fail fast(빠른 실패)**
- **Avoid magic numbers(매직 넘버를 피해라)**
- **One purpose for each variable(각 변수에 대한 하나의 목적)**
- **Use good names(좋은 이름을 사용하라)**
- **No global variables(전역 변수를 사용하지마라)**
- **Return results, don’t print them(결과를 출력하지말고 리턴하라)**
- **Use whitespace for readability(가독성을 위해서 공백을 사용하라)**

**→ 코드 리뷰 단원은 좋은 소프트웨어의 세가지 주요 속성과 연결된다.**

- **Safe from bugs(SFB) :** DRY 코드를 사용하면 버그가 다른 곳으로 전파될 염려 없이 그 부분만 수정할 수 있다. Fail Fast 원칙은 빠르게 버그를 감지한다. 전역 변수를 피하면 변수 값과 관련된 버그를 쉽게 지역과할 수 있다. (제한된 지역에서만 변경될 수 있기 때문에)
- **Easy to understand(ETU) :** 코드 검토는 실제로 애매하거나 복잡한 코드를 찾는 방법이다. 왜냐면 다른 사람들이 코드를 읽고 이해하려고 노력하기 때문이다. 주석을 사용하고, 매직 넘버를 피하고, 각 변수에 대해 목적을 유지하고, 좋은 이름을 사용하고, 공백을 잘 사용하면 코드에 대한 이해성을 높일 수 있다.
- **Ready for change(RFU) :** 코드 리뷰는 변경될 수 있는 사항을 예측하고 이를 방지하는 방법을 제안 할 수 있는 숙련된 소프트웨어 개발자가 수행할 때 도움된다. DRY 코드는 한 곳만 변경하면 되므로 변경 준비가 잘 되어있다. 결과를 출력하는 것이 아닌 반환하면 목적에 더 쉽게 적용 가능하다.