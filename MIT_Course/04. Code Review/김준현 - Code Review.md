# Reading 4: Code Review

# 서론

---

## 이번 Chapter의 목적

### Code Review

다른 사람이 쓴 코드를 읽고 토론하는 것

### General Principles of good coding

프로그램 언어나, 목적에 관계없이 모든 코드 리뷰에서 볼 수 있는 것

# Code Review

> Code review는 코드의 작성자가 아닌 사람들이 코드에 대해 신중하고, 체계적으로 연구하는 것
> 

---

## 목적

### 코드의 개선

버그를 찾고, 발생 가능한 버그를 예상하고, 코드의 명확성을 확인하고, 프로젝트의 스타일 표준과 일치하는지 확인

### 프로그래머의 개선

코드 리뷰는 새로운 언어의 기능, 프로젝트의 설계, 코딩 표준, 새로운 기술에 대하여 서로 배우고 가르치는 중요한 과정

특히, 오픈소스 프로젝트에서 더 많은 code review가 발생한다

# Style Standard

> 대부분의 기업과 큰 프로젝트들은 그들만의 코드 스타일이 존재한다
> 

---

스타일 가이드에는 정답이 존재하지 않으므로 자기 주관성을 유지하는 것이 중요하지만, 더 중요한 것은 진행하고 있는 프로젝트의 규칙을 준수하는 것이다

# Smelly Example #1

> 프로그래머들은 종종 삭제할 필요성이 있는 안좋은 코드를 “bad smell”이라고 묘사한다.(혹은 “Code hygiene”
> 

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

# Don’t Repaeat Yourself (=DRY)

> 중복되는 코드는 safety한 코드라고 하기에는 무리가 있다
> 

---

만약 동일하거나 비슷한 코드가 두개의 공간에 있다면, 두 코드들 모두 버그가 존재하며 한 쪽에 코드를 고치더라도 나머지 한쪽은 고쳐지지 않았다는 근본적인 문제가 존재한다

- 앞을 보지 않고 피하면서 길을 걷는것 처럼 중복을 피해라
- 복사-붙여넣기는 매력적인 프로그래밍 방법이지만, 그것을 사용할 때마다 조심해야 한다.

- `Don’t repeat yourself` 는 변경될 가능성이 있는 정보의 반복을 줄이고, 변경 가능성이 없는 추상화로 대체하거나 애초에 중복을 방지하는 데이터 정규화를 사용하는 것을 목표로 하는 소프트웨어 개발 원칙

# Comments Where Needed

> 좋은 소프트웨어 개발자는 그들의 코드에 comment를 달고, 현명하게 수행한다
> 

---

좋은 comment는 코드를 이해하기 쉽게 만들고, 버그로부터 안전하게 할 수 있으며(중요한 가정이 documented되었기 때문에) 변화에 적응하기 쉽다

### 중요한 comment의 한가지 종류는 specification이다

- 이는 method나 class 위에서 볼 수 있고, 그들의 동작을 기술한다
- Java에서는 관례적으로 Javadoc comment로 작성한다 (`/**` )

- Specifications는 가정을 문서화한다

### 또다른 중요한 종류는 코드의 출처나 기원을 명시하는 것

- 어딘가에서 복사하거나, 가져온 코드의 출처 명시
- 소프트웨어 개발자로 일하면서 매우 중요하다

- source를 documenting하는 이유는 저작권의 위반을 피하는 것이다
- Stack Overflow에 있는 작은 코드 조각들은 대표적으로 public domain에 존재한다
- 하지만, 다른곳에서 가져온 코드는 저작권이 존재하거나 훨씬 제한적인 오픈 소스 라이센스로 보호될 수 있다
- 또한 최신 버전이 아닐 수도 있다

### Comment를 올바르게 작성하는 법

```java
while (n != 1) { // test whether n is 1   (don't write comments like this!)
   ++i; // increment i
   l.add(n); // add n to l
}
```

- 우리는 이 코드를 읽는 사람이 최소한 Java를 알 것이라고 예상하기 때문에, 코드를 그대로 언어로 옮기는 것은 중요하지 않고, 필요하지 않은 작업이다

```java
sendMessage("as you wish"); // this basically says "I love you"
```

- 하지만 불분명한 의미를 가지는 코드에는 이러한 comment가 필요할 수 있음

# Fail Fast

> 코드는 가능한 한 빨리 버그를 드러내야 한다는 것이다
> 

---

- 문제가 빠르게 발생할 수록, 빠르게 찾고 고칠 수 있다
- 앞에서 static checking은 dynamic checking보다 빠르게 실패하고 dynamic checking은 다음 계산에 오류를 일의킬 수 있는 오답을 생성하는 것보다 빠르다
- 앞 예제인 `dayOfYear` 함수는 Fail Fast하지 않는다
    - 만일 올바르지 않은 순서의 argument로 통과하면 오답을 return할 것이다
    - 사실, 이 함수는 미국인이 아닌 사람들이 잘못된 순서로 argument를 통과할 가능성이 있다

# Avoid Magic Numbers

> Magic Number? 의미 있는 이름의 상수로 대체될 수 있는 숫자
> 

---

- 다른 상수들은 설명이 필요하다
- 한가지 방법은 comment를 작성하는 것이지만, 훨씬 더 좋은 방법은 그 숫자를 잘 설명할 수 있는 상수로 선언하는 것

- `month`를 2, …., 12 로 사용하는 것보다 `FEBUARY` , … , `DECEMBER` 등으로 작성하는 것이 훨씬 가독성이 좋다
- `days-of-month` 인 30, 31, 28은 `array, list, map` 등의 데이터 구조를 사용하여 `MONTH_LENGTH[month]` 처럼 표현하는 것이 가독성이 좋다
- 59, 90(`daysOfMonth`)는 특히 magic numbers를 나타내는 위험한 예시이다
    - comment 나 document가 존재하지 않고, 실제로는 프로그래머가 수작업으로 한 계산의 결과이다
    - 손으로 계산한 수를 hardcode화 하지 않는 것이 중요하다
    - `31 + 28` 과 같은 명시적인 계산은 `MONTH_LENGTH[JANUARY] + MONTH_LENGTH[FEBRUARY]` 와 같은 방식이 훨씬 명확하다

# One Purpose For Each Variable

> `dayOfMonth` 는 다른 값으로 계산되도록 재사용되었다. day of the month가 아닌 함수의 return value
> 

---

- parameter와 variable을 재사용하지 말아라
- 변수는 프로그래밍의 자원에서 불충분하지 않다
- 그들을 자유롭게 작성하고, 좋은 이름을 짓고, 그들이 필요로하지 않아지면 단지 사용을 멈춰라
- 해당 변수가 몇줄 아래에서 다른 의미로 사용되기 시작할 때, 코드를 읽는 사람들에게 혼란을 줄 것이다

- 문제를 쉽게 이해하는것 뿐만 아니라, bug-safety하고 ready-for-change 하도록 할 수 있음

- 특히, method parameter는 일반적으로 수정되지 않은 상태를 유지하여야 한다
    - 이것은 ready-for-change 하는 데에 중요하다, 미래에 method parameter의 초기값을 알고 싶을 수도 있기 때문에, 계산 중에 이를 수정하지 않아야 한다
    - `final` 키워드를 method parameter에 사용하는 것은 좋은 생각이며, 가능한 한 다른 변수에도 적용하는 것이 좋다
    - `final` 을 사용하면 statically check을 하기 때문이다

```java
public static int dayOfYear(final int month, final int dayOfMonth, final int year) { ... }
```

# Smelly Example #2

> #1에서는 윤년을 handling 하지 않았으므로, leap-year method를 고려함
> 

---

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

# Use Good Names

> 좋은 method와 variable 이름은 길고 스스로를 잘 표현한다
Good Names를 사용하여 readable한 코드를 작성한다면, Comment를 굳이 작성하지 않아도 된다
> 

---

```java
int tmp = 86400;  // tmp is the number of seconds in a day (don't do this!)
```

to:

```java
int secondsPerDay = 86400;
```

일반적으로, 변수 이름인 `tmp`, `temp`, `data` 는 프로그래머의 게으름의 증상 중 하나이다

- 모든 local variable은 일시적이며(temp), 모든 variable은 data이기도 하므로 이러한 이름들은 일반적으로 의미가 없다
- 긴 이름을 사용하고, 더욱 표현적인 이름을 사용하면 그것 자체로 코드를 명확하게 읽을 수 있을 것이다

```java
methodsAreNamedWithCamelCaseLikeThis()
variablesAreAlsoCamelCase
// method와 variable 이름은 다음과 같이 카멜케이스로 작성

CONSTRAINTS_ARE_IN_ALL_CAPS_WITH_UNDERSCORES
// 상수는 모두 대문자이며, 언더바로 분리된다

ClassesAreCapitalized
// 클래스 이름의 첫 글자는 대문자이다

packages.are.lowercase.and.separated.by.dots
// 패키지는 모두 소문자로 작성하며, dots(.)로 분리된다
```

Method 이름은 종종 동사 구문이다(`getData`, `isUpperCase`) 반면 variable과 class 이름은 대부분 명사구로 이루어져 있다

짧은 단어를 고르고, 간결하게 하되 줄임말은 피하는 것이 좋다

- `message` 는 `msg` 에 비해 명확하고, `word` 는 `wd` 에 비해 더 낫다
- 이는 영어가 모국어가 아닌 사람들이 줄임말에 대해 이해하는 것이 어려울 수 있기 때문이다
- 앞서 나왔던 `leap` method는 bad names 를 가지고 있다 이를 어떻게 고치면 좋을까?
- → `isLeapYear()`

# Use Whitespace to Help the Reader

> 일관적인 indentation(들여쓰기) 를 사용하라
> 

---

- Tab 문자를 사용하지 않고 공백 문자만 사용하라
    - 경우에 따라 공백 4칸이 삽입되기도 하며 2칸, 때로는 8칸으로 확장된다
    - Tab 키를 누를 때, 편집기가 공백 문자를 삽입하도록 설정해야 한다

# Smelly Example #3

---

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

## Don’t Use Global Variables → [[Link]](http://wiki.c2.com/?GlobalVariablesAreBad)

### Global Variable?

- variable, 변할 수 있는 것
- global, 프로그램의 어디에서든 접근할 수 있으며 값을 변경할 수 있음

### Global Variable in Java

Java에서는 Global Variable을 `public static` 이라고 선언한다

- `public` , 어디에서든 접근할 수 있도록 만들어 준다
- `static` , variable의 single instance가 있다

일반적으로 global variable을 parameter이나 return value로 변경하거나, 호출하는 method가 있는 object 안에서 사용한다

# Method Should Return Results, not Print Them

### `countLongWords` 는 ready-for-change를 할 수 없다

- 결과값을 console로 출력한다 (`System.out` )
- 만일 이를 계산을 위하여 사용한다면, 코드를 다시 작성하여야 한다

### 일반적으로 가장 높은 수준의 부분만 사람이나 console과 상호작용 하여야 한다

- Lower-level 부분은 반드시 그들의 `parameter` 와 같은 `input` 을 가지고 `result` 와 같은 `output` 을  `return` 하여야 한다
- 여기서 유일한 예외는 디버깅을 출력하는 것이며, 물론 콘솔에 출력할 수도 있다
    - 하지만 이는 설계의 일부가 아닌, 설계를 디버깅하는 방법의 일부여야 한다

# Summary

- Don’t Repeat Yourself (DRY)
- Comments where needed
- Fail Fast
- Avoid Magic Numbers
- One purpose for each variable
- Use Good names
- No global variables
- Return result, don’t print them
- Use Whitespace for readability

## Objectivity

### Safe from bugs

- 코드 리뷰는 버그를 찾기 위하여 사람(Reviewer)을 사용한다
- DRY한 코드는 버그가 다른곳으로 전파될 우려 없이 오직 한 위치에서만 코드를 수정할 수 있게 해준다.
- 가정을 Commenting하면 다른 프로그래머가 버그가 있는 코드를 작성할 가능성이 줄어든다
- Fail Fast 원칙은 가능한 한 빨리 버그를 탐지해 준다
- Global Variable의 사용을 지양하면 non-global variable은 오직 제한된 장소에서만 수정할 수 있기 때문에 variable value에 관련된 버그를 지역화할 수 있다

### Easy to understand

- 코드 리뷰는 불명확한 코드를 찾는 유일한 방법이다
    - 다른 사람들이 그것을 읽고 이해할 수 있기 때문이다
- 신중한(판단력 있는) Comment를 작성하는 것, Magic Number을 피하는 것, 각각 variable의 한가지 목적을 유지하는 것, 좋은 이름을 사용하고, Whitespace를 잘 사용하는 것은 모두 코드의 이해도를 높일 수 있다

### Ready for change

- 코드 리뷰는 경험있는 소프트웨어 개발자가 무엇이 바뀔지(ready-to-change) 예측하고, 이를 방지하는 방법을 제안할 때 도움이 된다
- 한 곳에서만 변경하면 되므로 DRY한 코드를 변경할 수 있다
- 결과를 출력(`System.out`)하는 대신 `return` 하면, 코드를 새로운 목적에 쉽게 적응시킬 수 있다

# Reference

---

[코드의 매직 넘버 (Magic Number) 란 무엇일까?](https://jake-seo-dev.tistory.com/155)

[](http://wiki.c2.com/?GlobalVariablesAreBad)

[The New Hacker's Dictionary  - = H =](https://www.outpost9.com/reference/jargon/jargon_23.html#TAG897)