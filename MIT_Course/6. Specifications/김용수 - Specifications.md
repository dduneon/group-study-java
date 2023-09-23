# Specifications

## **Objectives(목표)**

- **메소드 스펙에 `preconditions` 와 `postconditions` 를 이해하고 정확한 `specifications`를 작성 할 수 있다.**
- `**specification`에 대한 테스트를 작성할 수 있다.**
- **자바에서 확인되지 않은 예외와 확인된 예외의 차이점을 알 수 있다.**
- **특별한 결과에 대한 예외를 사용하는 법에 대한 이해를 할 수 있다.**

---

## Introduction

- `**Specifications`는 팀워크의 핵심이다:** Specification없이 메소드를 구현하는 것에 대한 책임을 지는 것은 불가능하다.
- **specification은 계약서 역할을 한다 :** implementer은 계약서를 충족 할 의무가 있으며 client는 계약서에 의졸 하여 메소드를 사용 할 수 있다.
- **실제로 법적인 계약서와 같이 specification은 양쪽 모두에게 요구된다 :** specification에 precondition이 명시되어있을 때 client또한 책임을 가지게된다.

- **이 단원에서 우리는 메서드의 spec에 의해 수행되는 역할을 살펴볼 것이다.**
- **preconditions와 Postconditions가 무엇인지 implementor과 client에게 의미하는 바가 무엇인지를 볼 것이다.**

---

# Part 1: Specifications

## Why specifications?

- **프로그램에서 발생하는 심각한 버그 중 다수는 코드 사이의 인터페이스 동작에 대한 오해로 발생한다.**
- **모든 프로그래머가 spec를 생각하지만 모든 프로그래머가 적는 건 아니다. 그 결과로 서로 다른 프로그래머들은 다른 spec를 생각하고 있다.**
- **프로그램이 오류가 발생할 때 어디서 오류가 발생하는지 파악하기 어렵다. 코드의 정확한 Spec를 통해 사람이 아닌 코드 조각에 책임을 맡길 수 있고, 어디를 수정해야할지 쉽게 파악할 수 있다.**
- **Spec은 코드를 읽는 작업을 줄여주기때문에 client에게 좋다. 만약 코드를 보는 것보다 스펙을 읽는게 쉽다는 확신이 들지 않는다면 표준 자바 spec를 보고 그것을 구현한 소스 코드와 비교해보는 것이 좋다.**

**코드 옆에 있는 `int` 의 사이즈 제한보다 크게 숫자를 표현할 수 있는 `BigInteger` 클래스의 예시이다.**

```java
// code
if (val.signum == 0)
    return this;
if (signum == 0)
    return val;
if (val.signum == signum)
    return new BigInteger(add(mag, val.mag), signum);

int cmp = compareMagnitude(val);
if (cmp == 0)
    return ZERO;
int[] resultMag = (cmp > 0 ? subtract(mag, val.mag)
                   : subtract(val.mag, mag));
resultMag = trustedStripLeadingZeroInts(resultMag);

return new BigInteger(resultMag, cmp == signum ? 1 : -1);
```

```java
public BigInteger add(BigInteger val)

Returns a BigInteger whose value is (this + val).

Parameters:
val - value to be added to this BigInteger.

Returns:
this + val
```

**→ `BigInteger.add`에 대한 스펙은 클라이언드가 이해하기 쉽고 만약에 `corner case`에 대한 질문이 있는 경우 `BigInteger` 클래스는 추가적으로 사람이 읽을 수 있는 문서를 제공한다.**

**만약 우리가 가진게 코드뿐이였다면 `BigInteger`의  생성자를 읽고, `compareMagnitude`, `subtract` 그리고 `trustedStripLeadingZeroInts`시작점을 읽어야 한다.** 

- **Spec은 메서드의 구현자가 클라이언트에게 따로 말 할 필요없이 구현을 변경을 할 수 있게 해주므로 구현자에게 좋다.**
- **또한, Spec를 사용하면 코드를 더 빠르게 만들 수 있다. 때로는 spec를 약하게 해준다면 구현을 훨씬 효율적으로 만들 수 있다.**
- **특히 `precondition` 은 더 이상 필요하지 않은 비싼 검사를 발생시키는 메소드가 호출될 수 있는 특정한 상태를 제외할 수 있습니다.**

![https://web.mit.edu/6.005/www/fa15/classes/06-specifications/figures/firewall.png](https://web.mit.edu/6.005/www/fa15/classes/06-specifications/figures/firewall.png)

- **계약은 구현자와 클라이언트 사이의 방화벽 역할을 한다. 이는 작동하는 장치에 대한 세부사항으로부터 클라이언트를 보호한다.**
- **Spec이 있는 경우는 프로시저의 작동하는 소스 코드를 읽을 필요가 없다. 그리고 장치를 사용하는 세부사항에 있어서 구현자를 보호한다.**

**→ 구현자는 모든 고객에게 어떻게 사용할 계획인지 물어볼 필요가 없기때문에**

- **이 방화벽은 변경 사항이 Spec을 준수하는 한 작동하는 코드부분과 클라이언트 코드 부분을 독립적으로 변경 할 수 있다. 각각 의무를 준수하기때문에**

---

## Behavioral equivalence

**두가지 메서드가 있는데 뭐가 같고 뭐가 다를까**

```java
static int findFirst(int[] arr, int val) {
    for (int i = 0; i < arr.length; i++) {
        if (arr[i] == val) return i;
    }
    return arr.length;
}

static int findLast(int[] arr, int val) {
    for (int i = arr.length -1 ; i >= 0; i--) {
        if (arr[i] == val) return i;
    }
    return -1;
}
```

**→ 코드 이름뿐만 아니라 실제 작동하는 행동도 다르다.**

- `**val` 이 없을 때, `findFirst`는 배열의 길이를 리턴하고 `findLast`는 -1을 리턴한다.**
- `**val`이 두개 있을 때, `findFirst`는 낮은 인덱스를 리턴하고 `findLast`는 높은 인덱스를 리턴한다.**

**→ 만약에 array에 `val`이 정확히 한 인덱스에서 발생하면 같은 index를 리턴을 하는 동작을 할 것이다.**

**동등성의 개념은 클라이언트에게 달려있다. 하나의 구현을 다른 구현으로 대체하고 이것을 허용하기 위해서 클라이언트가 지켜야하는 것은 무엇인지 정확하게 적혀있는 `Spec`가 필요하다.**

```java
// 이런 경우 Spec
static int find(int[] arr, int val)
  requires: val occurs exactly once in arr
  effects:  returns index i such that arr[i] = val
```

---

## Specification structure

→ 메소드의 Spec은 여러절로 구성된다.

- **Precondition :** `requires` 키워드로 나타낸다.
- **Postcondition :** `effects` 키워드로 나타낸다.

**`Precondition` : 클라이언트(메소드 호출자)의 의무이다. 즉, 메소드가 호출되는 상태에 대한 조건이다.**

`**Postcondition` : 메소드 구현자에 대한 의무이다. 호출 상태에 대한 precondition 이 유지되는 경우 메서드는 정확한 값을 반환하고, 지정된 예외를 발생시키고, 객체를 수정하거나 수정하지 않는 등의 방식으로 PostCondition을 지켜야한다.**

**전체적인 구조는 논리적인 함축이다. 만약 메소드가 호출 될 때, precondition이 유지된다면 메서드가 완료될 때 postcondtion도 유지되어야 한다.**

**메서드가 호출 될 때 precondtion이 유지되지 않으면 구현은 postcondtion에 바운드 되지 않는다.** 

**→ 종료하지 않음, 예외 발생, 임의 결과 반환, 임의 수정등을 포함한 모든 작업을 자유롭게 수행할 수 있다.**

![https://web.mit.edu/6.005/www/fa15/classes/06-specifications/figures/firewall-implies.png](https://web.mit.edu/6.005/www/fa15/classes/06-specifications/figures/firewall-implies.png)

![https://web.mit.edu/6.005/www/fa15/classes/06-specifications/figures/firewall-unsat.png](https://web.mit.edu/6.005/www/fa15/classes/06-specifications/figures/firewall-unsat.png)

---

## Specifications in Java

**일부 언어에서는 `preconditions` 과 `postconditions` 를 언어의 기본 부분으로 포함한다.**

**→ 클라이언트와 구현자 사이의 계약을 런타임 시스템이 자동으로 확인 할 수 있는 표현식으로 보기 위해서**

- **자바는 그정도로 발전하지 않았지만 static type 선언은 사실상 메소드의 precondition 및 postcondition의 일부이며, 이 부분은 컴파일러에 의해 자동을 확인되고 적용된다.**
- **계약의 나머지 부분(타입으로 작성되지 않는 부분)은 메소드 앞의 주석으로 설명해야 하며 이용하는 사람들이 이를 확인하고 지켜야하는 것에 의존한다.**
- **자바는 문서 주석에 대한 규칙이 있다. 규칙에서 파라미터는 `@param` 절로 설명되고 결과는 `@return` 및 `@throws`절로 설명된다.**
- **가능하면 precondition을 `@param`에 설명하고 postcondition을 `@return` 및 `@throws` 에 넣어야한다.**

**그래서 나온 spec**

```java
static int find(int[] arr, int val)
  requires: val occurs exactly once in arr
  effects:  returns index i such that arr[i] = val
```

**Java에서는 이렇게 된다.**

```java
/**
 * Find a value in an array.
 * @param arr array to search, requires that val occurs exactly once
 *            in arr
 * @param val value to search for
 * @return index i such that arr[i] = val
 */
static int find(int[] arr, int val)
```

**→ 자바 API문서는 Java 표준 라이브러리 소스 코드의 Javadoc 주석을 통해 생성된다. Javadoc으로 Spec을 문서화하면 사용자에게 유용한 정보를 표시 할 수 있으며 Java API 문서와 동일한 형식으로 HTML 문서를 생성 할 수 있다.**

---

## Null references

**자바에서는 객체 및 배열에 대한 참조가 특수 값인 `null` 을 참조 할 수 있다. 이는 참조가 객체를 가르키지 않음을 의미한다. NUll 값을 Java유형 시스템의 불행한 구멍이다.**

**Primitives type은 null일 수 없다.**

```java
int size = null;     // illegal
double depth = null; // illegal
```

**만약 Primitive type에 null이 들어간다면 컴파일러에서 static error이 발생해서 거부한다.**

**반면에 기본이 아닌 변수에는 null을 할당할 수 있다.**

```java
String name = null;
int[] points = null;
```

**컴파일러는 컴파일 타임에 이러한 코드를 받아들인다. 그러나 다음 참조 중 하나가 포함된 메서드를 호출하거나 필드를 사용할 수 없기때문에 런타임시 오류가 발생한다.**

```java
name.length()   // throws NullPointerException  
points.length   // throws NullPointerException
```

**→ 그리고 빈 문자열`""` 과 빈 배열의 null은 동일하지 않다. 빈 문자열과 빈 배열에서 메서드를 호출하고 필드에서 액세스 할 수 있다. 빈 배열 또는 빈 문자열의 길이는 0이다. null을 가르키는 문자열 변수의 길이는 `NullPointer-Exception`을 발생시킨다.**

`**Null` 값은 안전하지 않고 번거로으므로 디자인 차원에서 제거하는것이 좋다. 대부분에 훌륭한 자바 프로그래밍에서 null 값은 암시적으로 매개변수 및 반환 값으로 허용되지 않는다.**

**→ 따라서 모든 메서드에는 해당 객체 및 배열 파라미터에 대해 null이 아니라는 `precondition`이 암시적으로 있다.**

**→ 객체나 배열을 반환하는 모든 메서드에는 반환 값이 null이 아니라는 `postcondition` 있다.**

**만약 null 값을 허용하는 경우 명시적으로 이를 명시해야하며, 결과로 null 값을 반환할 수 있는 경우도 명시해야한다. 그러나 이는 좋은 상황이 아니다. → null을 피해야한다.**

**********************************자바에서는 `null` 유형을 직접 금지할 수 있는 것이 있다.**

```java
static boolean addAll(@NonNull List<T> list1, @NonNull List<T> list2)
```

**→ 컴파일 타임이나 런타임에 자동으로 확인할 수 있다.**

---

## What a specification may talk about

![https://web.mit.edu/6.005/www/fa15/classes/06-specifications/figures/firewall-talk.png](https://web.mit.edu/6.005/www/fa15/classes/06-specifications/figures/firewall-talk.png)

**메서드 Spec은 메서드의 `파라미터` 와 `return value`에 대해서는 말할 수 있지만, 메서드의 지역 변수나 메소드의 private field에 대해서는 말해서는 안된다.** 

**→ Spec를 읽는 사람에게는 구현이 보이지 않는 것을 고려해야한다.**

**Java에서는 Javadoc도구가 코드에서 `Spec Comment` 를 추출하여 HTML로 렌더링하기 때문에 사양을 읽는 사람이 메소드의 소스 코드를 읽을 수 없는 경우가 많다.**

---

## ****Testing and specifications****

**테스트에서는 Spec만을 염두에 두고 선택하는 `black box`와 실제 구현에 대한 지식을 바탕으로 선택하는 `glass box test`에 대해 이야기한다.**

**→ But, glass box test 또한 spec를 따라야한다는 점을 유의해야한다.**

**우리의 구현은 Spec가 요구하는 것보다 더 강력한 보장을 제공할 수 있고, Spec이 정의되지 않은 경우 특별한 동작을 가질 수 있다. → 그러나 테스트 케이스는 그러한 동작에 의존해서는 안됨**

**즉, 테스트 케이스는 다른 모든 클라이언트와 마찬가지로 Spec를 준수해야한다.**

예를 들어 지금까지와 약간 다른 `find spec`를 테스트한다고 할 때,

```java
static int find(int[] arr, int val)
  requires: val occurs in arr
  effects:  returns index i such that arr[i] = val
```

**→ `spec` 에서는 `val` 을 찾아야한다는 점에서 강력한 전제 조건이 있다. 그리고 배열에 `val` 이 두 번 이상 나타나면 이 spec은 어떤 특정 인덱스가 반환되는지에 대해 아무 것도 말하지 않는다는 점에서 상당히 약한 `Postcondition`을 가지고 있다.**

**→ 만약 가장 낮은 인덱스를 반환하도록 구현하더라도 테스트 케이스는 특정한 행동에 대해서 가정할 수 없다.**

```java
int[] array = new int[] { 7, 7, 7 };
assertEquals(0, find(array, 7));  // bad test case: violates the spec
assertEquals(7, array[find(array, 7)]);  // correct
```

**마찬가지로, val을 찾을 수 없을 때 오해의 소지가 있는 인덱스를 반환하는 대신 예외를  발생시키도록 구현한 경우에도 테스트 케이스는 해당 메서드를 가정 할 수 없다.**

**→ glass box testing가 spec를 뛰어넘을 수 없다는건 무엇을 의미할까? 이는 구현의 다양한 부분을 실행하는 새로운 테스트 케이스를 찾으려고 노력하지만 여전히 구현과는 독립적인 방식으로 테스트 케이스를 확인한다.**

**즉, 그럼에도 구현은 생각하지 않고 행동에 대해서 테스트 케이스를 만든다는 것을 의미함.**

---

## Testing units

테스트에서 사용했던 test web 예제

```java
/** @return the contents of the web page downloaded from url */
public static String getWebPage(URL url) { ... }

/** @return the words in string s, in the order they appear,
 *          where a word is a contiguous sequence of
 *          non-whitespace and non-punctuation characters */
public static List<String> extractWords(String s) { ... }

/** @return an index mapping a word to the set of URLs
 *          containing that word, for all webpages in the input set */
public static Map<String, Set<URL>> makeIndex(Set<URL> urls) { 
    ...
    calls getWebPage and extractWords
    ...
}
```

**→ 우리는 이 부분을 다루면서 프로그램의 각 모듈에 대한 테스트를 별도로 작성해야한다는 것을 의미하는 단위 테스트에 대해서 이야기 했다. 좋은 단위 테스트는 단일 Spec에 대해서만 집중한다.**

- **우리의 테스트는 거의 자바 라이브러리 메서드의 Spec에 의존하지만 우리가 작성한 메서드에 대한 `unit test`는 다른 메서드가 해당 Spec을 만족하지 못하더라도 실패해서는 안된다.**
- **즉, 예제에서 보았듯이 `getWebPage` 에 대한 `postcondition`이 만족하지 않더라도 `extractWords`에 대한 테스트가 실패해서는 안된다.**

**모듈 조합을 사용하는 좋은 통합 테스트는 서로 다른 메서드가 호환 가능한 Spec을 가지고 있는 확인합니다.**

**즉, 서로 다른 메서드의 호출자와 구현자는 상대방이 기대하는대로 값을 전달하고 반환한다.**

**통합 테스트는 체계적으로 설계된 단위 테스트를 대체할 수 없다. 예제에서 `makeIndex` 호출을 통해서만 테스트 한다면 `extractWords` 입력 공간의 잠재적으로 작은 부분, 즉 `getWebPage`의 가능한 입출력 부분에서만 테스트 할 것 이다.**

**→ 이를 통해 우리는 프로그램의 다른 곳에서 다른 목적으로 `extractWords`를 사용하거나 `getWebPage`가 새로운 형식으로 작성된 웹 페이지를 반환하기 시작할 때 버그가 숨을 수 있는 장소를 남겨두었다.**

---

## Specifications for mutating methods

**→ 이전에 우리는 변경 가능한 객체와 변경이 불가능한 객체에 대해서 논의했었지만 `find`의 스펙에서의 postcondition에서는 Side-effect가 어떻게 일어나는지에 대한 설명을 제공해주지 않았다.**

객체를 변경하는 메서드에 대한 Spec

```java
static boolean addAll(List<T> list1, List<T> list2)
  requires: list1 != list2
  effects:  modifies list1 by adding the elements of list2 to the end of
              it, and returns true if list1 changed as a result of call
```

**자바 `LIst` 인터페이스로부터 단순화하여 가져왔다.** 

- **`postcondition`에서는 두가지 제약이 있다.**
1. **list1이 어떻게 변경이 되는지에 대해**
2. **return 값이 어떻게 결정되는지에 대해서**

- **`precondition`: 목록의 요소를 자체에 추가하려고 하면 메서드의 동작이 정의되지 않음을 알 수 있다.**

**메소드 구현자가 이 제약 조건을 부과하려는 이유:** 

1. **만약 `list1`와 `list2`가 다르다면 `list1` 에 `list2`값을 추가해준다.(list2 값 끝까지)**
2. **만약 `list1` 과 `list2`가 동일하다면 이 알고리즘은 종료되지 않는다. 이러한 결과 때문에 `precondition` 에 명시됐다.**

파라미터 ********************************변경 가능한 예********************************

```java
static void sort(List<String> lst)
  requires: nothing
  effects:  puts lst in sorted order, i.e. lst[i] <= lst[j]
              for all 0 <= i < j < lst.size()
```

**************************************************************************************************************파라미터를 변경하지 않는 메서드 예**************************************************************************************************************

```java
static List<String> toLowerCase(List<String> lst)
  requires: nothing
  effects:  returns a new list t where t[i] = lst[i].toLowerCase()
```

`**null` 과 달리 명시되지 않는 한 암묵적으로 허용되지 않는다고 말한 것 처럼, 달리 명시되지 않는 한 변형도 허락 하지 않는다.**

`**toLowerCase` 의 Spec에서는 “lst는 수정되지 않는다.” 라고 효과적으로 명시 할 수 있지만 변형을 설명하는 `postcondition` 이 없는 경우 입력의 변형을 요구하지 않는다.**

---

# Part 2: Exceptions

**이제 Spec를 작성하고 클라이언트가 메서드를 어떻게 사용할지를 알고 있으니, 버그로부터 안전하고 이해하기 쉬운 방식으로 Exceptional case를 다루를 방법에 대해 논한다.**

---

## Exceptions for signaling bugs

**우리는 지금까지 자바 프로그래밍에서 Exception을 본 적 있음(가장 일반적인 예외)**

- `**ArrayIndex-OutOfBoundsException` : 배열에서 유효한 범위를 벗어날 때**
- `**NullPointerException**`: `**null` 객체에서 메서드를 참조하려고 할 때**

**이러한 예외는 일반적으로 코드에 버그가 있음을 나타내며 예외를 던졌을 때 자바에 의해 표시되는 정보를 버그를 찾고 고치는데 도움이 된다.**

**다른 예시**

- `**ArithmeticException` : 정수를 0으로 나눈 것과 같은 산술적 오류가 발생**
- `**NumberFormatException` : `Integer.parseInt` 와 같은 메서드에 정수로 분석되지 않는 문자열을 전달한 경우**

---

## Exceptions for special results

**`Exception`은 단지 버그를 표시하기 위한게 아니다. 특별한 결과가 있는 절차를 포함하는 코드의 구조를 개선하는데 사용되곤한다.**

**특별한 결과를 처리하는 일반적인 방법은 특별한 값을 반환하는 것이다.**

**자바 라이브러리의 룩업 연산의 설계 :** 

- **양의 정수를 기대할 때 -1의 index를 얻는다.**
- **객체를 예상할 때 `null` 값을 리턴한다.**

************************************************************→ 이러한 방법은 두 가지 문제가 있다.************************************************************ 

1. **return value check**
2. **easy to forget to do it**

**(이러한 이유로 예외를 사용하면 컴파일러의 도움을 받을 수 있다.)**

**또한 `special value` 를 찾는 것은 쉽지 않다. lookup method가 있는 `BitrhdayBook` 클래스가 있을 때**

```java
// 메소드 시그니처
class BirthdayBook {
    LocalDate lookup(String name) { ... }
}
-> LocalDate는 자바 API의 일부이다.
```

**만약에 BitrthdayBook에 이름이 적혀 있지 않은 사람은 어떻게 해야 할까?**

**실제 날짜로 사용되지 않는 특별한 날짜를 돌려줄 수 있다.**

**ex) 1960년에 작성된 프로그램은 나중에도 실행되지 않을 것으로 예상해서 99/9/9를 반환했다(잘못된 사례)**

********************************************더 나은 접근 방법********************************************

```java
LocalDate lookup(String name) throws NotFoundException {
        ...
        if ( ...not found... )
            throw new NotFoundException();
        ...
```

**************************************그리고 메서드 호출자는 `catch`로 예외를 처리해준다.**

```java
BirthdayBook birthdays = ...
    try {
        LocalDate birthdate = birthdays.lookup("Alyssa");
        // we know Alyssa's birthday
    } catch (NotFoundException nfe) {
        // her birthday was not in the birthday book
    }
```

**이젠 Special Value를 리턴하지 않고 그와 관련된 검사도 하지 않는다.**

---

## Checked and unchecked exceptions

**우리는 exception의 두가지 목적을 발견했다.(`special result`와 `bug detection`)**

`**checked exceptions` : 컴파일러에 의해 확인된 예외.**

- **메소드에서 `checked exception`가 발생할 수 있는 경우 해당 메서드 시그니처에서 선언해야한다. `NotFoundExcetion` 은 확인된 예외이므로 시그니처 끝에 `throws Not FoundException` 을 적어주어야 한다.**
- **만약 한 메서드에서 `checked exception` 를 발생시킬 수 있는 다른 메서드를 호출하는 경우 해당 메서드를 처리하거나 Exception 자체를 선언해야한다. 로컬에서 발견되지 않으면 호출자에게 전파되기 때문이다.**

**따라서 `BirthdayBook`의 `lookup` 메서드를 호출하고 `NotFoundException` 을 처리하는 것을 까먹는다면 컴파일러에서 코드를 거부할 것이다.**

**→ 이 기능은 나타날 수 있는 예외를 처리할 수 있게 보장하기때문에 매우 유용함**

******************************************************************************************************반대로, Unchecked exception은 버그를 알리는 데 사용된다.****************************************************************************************************** 

- 이러한 예외는 최상위 수준을 제외하고는 코드에서 처리되지 않을 것이다.
- `**Call Chaining` : 모든 메소드가 하위 호출 단계에서 발생할 수 있는 모든 종류의 버그 관련 Exception( index out of bounds, null pointers, illegal arguments, assertion faulures, 등등) 을 선언하는 걸 원하지 않는다.**

`**Call Chaining` : 프로그래밍에서 사용되는 용어로, 메서드 또는 함수 호출을 연이어 연결해서 사용하는 방식. 이러한 방식은 코드를 간결하게 작성하고 읽기 쉽게 만들어준다.**

**********************************결과적으로 `Unchecked exception` 의 경우는 컴파일러가 `try-catch` 또는 `throws` 선언을 확인하지 않는다.** 

**자바에서는 여전히 메서드 시그니처의 일부로 Unchecked Exception에 대한 `throws` 절을 작성할 수 있지만, 효과가 없으므로 권장하지 않는다.**

**모든 예외에는 메시지가 연결 될 수 있다. 생성자에게 제공되지 안흔ㄴ 경우에 메시지 문자열에 대한 참조는 `null` 이다.**

---

## Throwable hierarchy

**자바가 예외의 선택여부를 결정하는 방법을 이해하기 위해서 Java 예외에 대한 클래스 계층 구조를 살펴봐야한다.**

![https://web.mit.edu/6.005/www/fa15/classes/06-specifications/figures/throwable.png](https://web.mit.edu/6.005/www/fa15/classes/06-specifications/figures/throwable.png)

`**Throwable` : 던지거나 잡힐 수 있는 오브젝트이다.**

- **`Throwable` 의 구현은 예외가 던져진 지점의 스택 추적과 함께 예외를 설명하는 문자열을 기록한다.**
- `**throw` 혹은 `catch` 문에 사용되거나 메서드의 `throw`의 선언된 모든 개체는 `Throwable` 의 서브 클래스여야한다.**

`**Error` : `Throwable` 의 하위 클래스이다.**

- `**StackOverflowError` 혹은 `OutOfMemoryError` 와 같이 자바 런타임 시스템에서 생성된 오류를 위해 예약됐다.**
- **어떤 이유로 `AssertionError` 은 런타임이 아니라 사용자의 코드를 나타내더라도 `Error`을 `extends` 한다.**
- **오류는 복구 할 수 없는 것으로 간주되어야하며 일반적으로 발견되지 않는다.**

**자바에서 `checked exception` 과 `unchecked exception` 을 구분하는 법**

- `**RuntimeException`, `Error` 그리고 그들의 `subclass`는 unchecked exception**

→ 컴파일러는 `throws`절에 그걸 선언하는 것을 요구하지않고 해당 메서드의 호출자가 이를 캐치하거나 선언하는것을 요구하지 않음

- **다른 모든 throwables(`Throwable` , `Exception` 그리고 위에서 말 한 서브 클래스들을 제외한 모든 클래스)는 checked exceptions**

→ 컴파일러는 이 예외들이 발생 할 수 있을 때 `catch` 하거나 `declared` 하는 것을 요구한다.

**자신만의 예외를 정의할 때 `RuntimeException`(unchecked exception으로 만들기 위해서) 또는 `Exception`(checked exception으로 만들기 위해서)의 하위 클래스 중 하나를 선택해야 한다.**

**프로그래머는 일반적으로 `Error` 혹은 `Throwable`를 서브 클래스 하지 않는다.** 

**→ 그것은 자바 자체적으로 예약어이기 때문에**

---

## Exception design considerations

**특별한 결과(예상되는 상황)에는 `checked exception`를 사용하고, 버그 발생(예상치 못한 실패)에는 `unchecked exception`을 사용하는 규칙만이 전부는 아니다.**

- **성능 저하 외에도 예외로 인해 또 다른 (더 심각한) 비용이 발생한다. 즉, 메소드 설계와 메서드 사용 모두에서 사용하기엔 어렵다.**
- 만약 메서드가 새로운 `Exception` 을 갖도록 설계하는 경우 예외에 대한 새 클래스를 만들어야 한다.

**→ 만약 `checked exception` 을 던지는 메서드를 호출하는 경우, exception을 던지지 않을 걸 알고 있을지라도 , `try-catch` 문을 통해서 감싸주어야한다.(딜레마를 일으킨다.)**

**예를 들어, queue abstraction를 설계 할 때, 큐가 비어있을 때, 큐에서 pop을 사용한다면 checked exception 을 던져야 할까?** 

**예외를 던질 때 까지 pop을 하는 프로그래밍 스타일을 지원한다고 가정 한다. `checked exception`을 선택해야한다.**

**일부 클라이언트는 `pop` 을 사용하기전에 클라이언트가 queue가 비어있는지 확인 후 비어있지 않은 경우에만 `pop` 을 하고 싶어한다. 그래도 여전히 클라이언트는 `try-catch` 문을 통해서 호출을 감싸주어야한다.**

**이는 좀 더 정교한 규칙을 제시한다.**

- `**unexpected exception`은 예상치못한 실패를 알리는 경우에만 사용하거나 예외를 방지할 수 있는 편하고 저렴한 방법이 있기 때문에 일반적으로 클라이언트가 예외를 발생하지않도록 보장하는 코드를 작성할 것으로 예상되는 경우에만 사용해야한다.**
- **그렇지 않다면 `checked exception` 을 사용해야한다.**

**이 규칙을 적용한 가상의 메서드 예시:**

- `**Queue.pop()` 을 호출 했을 때, 큐가 비어있다면 `unchecked EmptyQueueException` 를 발생 시킨다. 호출자가 `Queue.size()` or `Queue.isEmpty()` 와 같은 호출을 통해 이를 방지할 것으로 기대하는 것이 합리적이기 때문이다.**
- `**Url.getWebPage()`는 웹 페이지를 검색 할 수 없을 때, `checked IOException` 를 던진다. 호출자가 이를 방지하는 것이 쉽지 않기 때문이다.**
- `**int integerSquareRoot(int x)` 는 `x`가 적분 제곱근이 없을 때 `checked NotPerfectSquareException` 를 던진다. `x`가 완전 제곱근인지 테스트하는 것은 실제 제곱근을 찾는 것만큼 어렵기 때문에 호출자가 방지하는 것이 쉽지 않기 때문이다.**

**→ 자바에서 예외를 사용하는 비용은 많은 자바 API가 null 참조를 special value로 사용하는 이유 중 하나이다. 잘 쓴다면 그건 잘못된 것이 아니다.**

---

## Abuser of exceptions

```java
try {
    int i = 0;
    while (true)
        a[i++].f();
} catch (ArrayIndexOutOfBoundsException e) { }
```

> **이 코드는 무엇을 하는가? 검사상 이것 목표가 명백하지 않기 때문에 사용하지 않을 이유가 충분하다. 무한 루프는 배열에 인덱스 밖에서 접근하려고 할 때 `ArrayIndex-OutOfBoundsException`을 던지고 catch하여 안전하게 종료된다.**
> 

위 코드**는 이것과 같다고 추정된다.**

```java
for (int i = 0; i < a.length; i++) {
    a[i].f();
}

Or

for (T x : a) {
    x.f();
}
```

**`Spec`은 프로시저의 구현자와 클라이언트 사이에서 중요한 방화벽 역할을 한다.**

**클라이언트는 소스 코드를 보지 않고 프로시저를 사용하는 코드를 자유롭게 사용할 수 있고 구현자는 메서드를 어떻게 사용할지 모르는 상태에서 자유롭게 작성할 수 있다.**

`**Spec` 이 MIT과정에서 어떤 도움이 되는가?**

- **Safe form bugs(SFB) : 좋은 스펙은 클라이언트와 구현자가 의존하는 상호 가정을 명확하게 문서화한다. 보통 버그는 인터페이스의 불일치로 발생하기 때문에 스펙이 존재하면 이는 감소한다. 단순히 사람이 읽을 수 있는 주석이 아닌 `Static typing` 와 `exception`를 통해 버그를 훨씬 줄일 수 있다.**
- **Easy to understand(ETU) : 짧고 간단한 스펙은 구현 자체보다 이해하기 쉬우며 사용자가 코드를 읽지 않아도 된다.**
- **Ready for change(RFC) : 스펙은 코드의 여러 부분간의 계약을 만들어주므로 요구 사항을 충족해준다면 언제든지 변경할 수 있다.**