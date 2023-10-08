# Abstraction Functions & Rep Invariants

## Objective

- Invariants
- representation exposure
- abstraction functions
- representation invariants

**이 단원에서는 abstraction function과 representation invaraints의 개념을 통해 클래스가 ADT를 구현하는 것이 무엇을 의미하는지에 대한 공식적인 수학적인 아이디어를 다룬다.**

**abstract function은 abstract data type에 대한 equality operation을 명확하게 정의하는 방법을 제공한다.**

**representation invariant을 사용하면 손상된 데이터 구조로 인해 발생하는 버그를 더 쉽게 잡을 수 있다.**

---

## ****Invariants****

**좋은 `abstract data type` 을 만드는 요소에 대한 논의를 다시 시작하면, 가장 중요한 속성은 그 자체 `invariant` 를 보존한다는 것이다.** 

- `**invariant`(불변성)은 프로그램의 가능한 모든 런타임 상태에 대해 항상 true인 프로그램의 속성이다.**
- `**Immutability` 는 우리가 이미 접한 `Invariant` 이다. → 일단 생성되면 불변 객체는 전체 수명 동안 항상 동일한 값을 나타내야 한다.**
- **ADT가 자체 `Invariant` 를 유지한다는 것은 ADT가 자체적으로 책임이 있다는 것이다. → 고객의 좋은 행동에 의지하지 않음**

**ADT가 `own Invariant` 를 유지하면 코드에 대한 추론이 훨씬 쉬워진다. 문자열이 절대 변경되지 않는다는 사실을 알고 있다면 문자열을 사용하는 코드를 디버깅할 때 또는 문자열을 사용하는 다른 ADT에 대한 `Invariant`설정 하려고 할 때 그러한 가능성을 상관쓰지 않아도 된다. 클라이언트가 변경하지 않겠다고 약속한 경우에만 변경할 수 없음을 보장하는 문자열과 비교해보면 알 수 있다.**

### Immutability

```java
/**
 * This immutable data type represents a tweet from Twitter.
 */
public class Tweet {

    public String author;
    public String text;
    public Date timestamp;

    /**
     * Make a Tweet.
     * @param author    Twitter user who wrote the tweet.
     * @param text      text of the tweet
     * @param timestamp date/time when the tweet was sent
     */
    public Tweet(String author, String text, Date timestamp) {
        this.author = author;
        this.text = text;
        this.timestamp = timestamp;
    }
}
```

**이러한 Tweet개체가 Immutable하다는 것을 어떻게 보장할 수 있을까? 즉, 일단 클래스가 생성되면 작성자, 메시지 및 날짜가 절대 변경될 수 없다.**

`**Immutability` 에 대한 첫 번째 위협은 클라이언트가 실제로 해당 필드에 직접 접근 할 수 있다는 사실에서 비롯된다. 따라서 다음과 같은 코드를 작성하는 것을 막을 수 없다.**

```java
Tweet t = new Tweet("justinbieber", 
                    "Thanks to all those beliebers out there inspiring me every day", 
                    new Date());
t.author = "rbmllr";
```

**이는 `representation exposure`의 한 예시이다. 즉, 클래스 외부의 코드가 표현을 직접 수정할 수 있음을 의미한다. 이와 같은 Rep exposure는 `immutability`뿐만 아니라 표현 독립성도 위협한다. 해당 필드에 직접 접근하는 모든 클라이언트에게 영향을 주지 않고는 트윗 구현을 변경할 수 없다.**

Java에서 제공하는   Rep exposure를 처리할 수 있는 언어 메커니즘

```java
public class Tweet {

    private final String author;
    private final String text;
    private final Date timestamp;

    public Tweet(String author, String text, Date timestamp) {
        this.author = author;
        this.text = text;
        this.timestamp = timestamp;
    }

    /** @return Twitter user who wrote the tweet */
    public String getAuthor() {
        return author;
    }

    /** @return text of the tweet */
    public String getText() {
        return text;
    }

    /** @return date/time when the tweet was sent */
    public Date getTimestamp() {
        return timestamp;
    }

}
```

`**private` 와 `public` 키워드는 클래스 내에서만 액세스할 수 있는 필드, 메서드를 나타내고 클래스 외부에서 액세스 할 수 있는 필드와 메서드를 나타낸다.**

`**final` 키워드는 객체가 생성된 후 `immutable field`가 다시 할당되지 않도록 보장한다.**

**하지만 이게 전부는 아니다. (표현은 여전히 노출된다.)**

`**Tweet` 을 사용하는 완벽하게 합리적인 클라이언트 코드를 생각해보아라.**

```java
/** @return a tweet that retweets t, one hour later*/
public static Tweet retweetLater(Tweet t) {
    Date d = t.getTimestamp();
    d.setHours(d.getHours()+1);
    return new Tweet("rbmllr", t.getText(), d);
}
```

`**retweetLater` 는 트윗을 받아 동일한 메시지(리트윗이라고 함)가 포함된 다른 트윗을 반환해야 하지만 한시간 후에 반환된다.**

`**retweetLater` 메서드는 트위터에 셀럽들이 말하는 재미있는 말을 자동으로 에코하는 시스템의 일부일 수 있다.**

![http://web.mit.edu/6.005/www/fa15/classes/13-abstraction-functions-rep-invariants/figures/retweetLater.png](http://web.mit.edu/6.005/www/fa15/classes/13-abstraction-functions-rep-invariants/figures/retweetLater.png)

**→ 여기서 문제는 `getTimestamp` 메서드는 tweet에서 사용하는 `Date t`객체에 대한 레퍼런스를 반환한다.(`aliasing`**)

**따라서 해당 날짜 객체가 `d.setHours()`를 사용 한다면 이는 다이어그램에서 보이는 것 처럼 스냅샷에 영향을 미친다.**

`**Tweet`의 `immutability invariant` 가 깨졌다.**

**→ Tweet이 `immutability` 를 의존하는 `mutable` 객체에 대한 reference를 유출했다는 것이다. → 우리가 Tweet가 immutable를 보장할 수 없도록 rep(표현)을 유출했기 때문에**

**우리는 `defensive copying` 를 사용해서 이러한 유형의 노출을 막을 수 있다.**

**즉, rep에 대한 참조가 유출되는 것을 막기 위해서 변경 가능한 객체의 복사본을 만든다.**

```java
public Date getTimestamp() {
    return new Date(Date.getTime());
}
```

- `**Mutable` type에는 기존 인스턴스의 값을 복제하는 새 인스턴스를 만들 수 있는 복사 생성자가 있는 경우가 많다.**
- **이 경우 `Date` 복사 생성자는 1970년 1월 1일 이후 밀리초 단위로 측정된 타임스팸프 값을 사용함**
- **변경 가능한 객체를 사용하는 다른 방법으로는 `clone()` 가 있다.**

**우리는 `getTimestamp`에 대한 값을 반환 할 때 `defensive copying` 를 사용했다. 그러나 여전히 rep는 노출된다. 다음 코드를 고려해보아라**

```java
/** @return a list of 24 inspiring tweets, one per hour today */
public static List<Tweet> tweetEveryHourToday () {
    List<Tweet> list = new ArrayList<Tweet>(); 
    Date date = new Date();
    for (int i=0; i < 24; i++) {
        date.setHours(i);
        list.add(new Tweet("rbmllr", "keep it up! you can do it", date));
    } 
    return list;
}
```

**이 코드는 하루 24시간 동안 단일 Date객체를 업데이트해서 매 시간 마다 트윗을 생성하려고 한다.**

**그러나 Tweet의 생성자는 전달된 값을 저장하므로 스냅샷 다이어그램은 이렇게 24개의 모든 Tweet객체가 동일한 시간을 가르킨다.**

**이번에도 트윗의 `Immutability`가 위반되었다. 이번에는 생성자에서 `defensive copying`를 하여 문제를 해결 할 수 있다.**

```java
public Tweet(String author, String text, Date timestamp) {
    this.author = author;
    this.text = text;
    this.timestamp = new Date(timestamp.getTime());
}
```

**일반적으로 모든 ADT작업의 파라미터 타입과 리턴 타입을 주의 깊게 검사해야한다. `immutable type` 을 다룰 경우 구현이 해당 표현에 대한 직접적인 참조를 반환하는지 확인해야한다. 그렇게 된다면 rep가 노출된다.**

**이것이 낭비라고 판단할 수 있다. → 왜 모든 날짜에 대한 복사본을 만드는가? → 스펙을 잘 작성한다면 문제를 해결할 수 없을까?**

```java
/**
 * Make a Tweet.
 * @param author    Twitter user who wrote the tweet.
 * @param text      text of the tweet
 * @param timestamp date/time when the tweet was sent. Caller must never 
 *                   mutate this Date object again!
 */
public Tweet(String author, String text, Date timestamp) {
```

- **이런 방법은 가끔 더 좋은 방법이 없을 때 사용된다.**

**ex) 변경 가능한 객체가 너무 커서 효율적으로 복사가 될 수 없는 경우**

- **프로그램에 대해 추론하는 능력과 버그를 피하는 능력의 비용은 엄청나다. 설득력이 없는 스펙의 경우 ADT이 자체적으로 `immutability`를 보장하는 것은 항상 가치가 있으며 rep 노출을 방지 하는 것은 필수적이다.**

**더 나은 방법은 `immutable` 유형을 선호한다. `[java.util.Date](http://java.util.Date)` 같은 mutable type 대신에 `java.util.ZonedDateTime` 와 같은 immutable type을 사용했다면 더 이상 rep노출에 대한 걱정을 없었을 것이다.**

### ****Immutable Wrappers Around Mutable Data Types****

Java 컬렉션 클래스는 immutable Wrapper이라는 좋은 대안을 제공한다.

`**Collections.unmodifiableList()` 는 실제로는 (mutable)List를 가져오지만 mutators가 비활성화된 객체로 래핑된다. - `set()` , `add()`, `remove()` 사용시 예외를 던짐.**

**따라서 mutator를 통해 변경 불가능한 리스트를 얻을 수 있다.**

**여기서 단점은 런타임에는 `immutablity` 를 얻을 수 있지만 컴파일 타임에는 얻을 수 없다. `unmodifiable list`를 `sort()` 하려고 한다면 Java는 컴파일 시 경고를 표시하지 않는다.**

**→ 런타임에 예외가 발생한다.(그래도 아무것도 안쓰는 것보다는 낫다)**

---

## ****Rep Invariant and Abstraction Function****

Abstract data type의 기초가 되는 이론을 더 자세히 살펴 볼 것이다. 

**ADT를 생각할 때 두 값 공간간 관계를 고려하는 것이 도움이 된다.**

**Rep 값의 공간은 실제 구현 엔티티의 값으로 구성된다. 간단한 경우에는 추상 유형이 단일 객체로 구현되지만 실제로는 복잡하다.**

**문자 집합을 표현하기 위해서 문자열을 사용한다고 가정**

```java
public class CharSet {
    private String s;
    ...
}
```

`Representation space` R에는 문자열이 포함되고 `Abstract space` A에는 수학적으로 표현된 문자 집합이 포함된다. Rep 값이 나타내는 추상 값까지 괄호를 사용해서 표시 할 수 있다.

![http://web.mit.edu/6.005/www/fa15/classes/13-abstraction-functions-rep-invariants/figures/charset-af-ri.png](http://web.mit.edu/6.005/www/fa15/classes/13-abstraction-functions-rep-invariants/figures/charset-af-ri.png)

- **모든 abstract value는 rep value로 매핑 :** abstract type를 구현하는 목적은 abstract값에 대한 작업을 지원하는 것이다. 그렇다면 우리는 가능한 모든 abstract value를 생성 하고 조작 할 수 있어야 하며, 따라서 그 값은 표현 가능해야 한다.
- **몇개의 abstract value는 둘 이상의 rep value에 매핑됨 :** 이는 rep가 엄격한 인코딩이기 때문이다. 순서가 지정되지 않는 문자 집합을 문자열로 표현하는 방법은 여러가지가 있다.
- **모든 rep value가 매핑되는 것은 아님 :** 이 경우 문자열 “abbc”는 매핑되지 않는다. 문자열에 중복된 내용이 포함되서는 안된다고 결정했다. 이렇게 하면 특정 문자의 첫 번째 인스턴스에 도달할 때 제거 메소드를 종료할 수 있다. 최대 한 개가 있을 수 있다는 것을 알고 있다.

실제로 우리는 두 공간의 몇가지 요소와 관계만 설명할 수 있다. 그래프 전체는 무한하다. 

→ 두가지로 설명됨.

1. **rep value을 대표하는 abstract value에 매핑하는 abstract function**

> AF : R → A
> 

함수가 전사(호출됨)이고 전단사(일대일)일 필요는 없다. 종종 부분적이다.

1. rep value을 boolean으로 매핑하는 rep invariant

> RI : R → boolean
> 

Rep value r의 경우 RI(r)은 r이 AF에 의해 매핑되는 경우에만 true이다.

**rep invariant와 abstract function 모두 코드에서 선언 바로 옆에 문서화 되어야 한다.**

```java
public class CharSet {
    private String s;
    // Rep invariant:
    //    s contains no repeated characters
    // Abstraction Function:
    //   represents the set of characters found in s
    ...
}
```

`**abstract function`과 `rep invariant` 에 대한 일반적인 혼동은 rep와 abstract value의 선택에 의해 결정되거나 심지어 abstract value space에 의해 결정되는 것이다. 그렇게 된다면 이미 다른 곳에서 사용할 수 있는 중복된 내용을 말하고 있기 때문에 거의 쓸모가 없을 것이다.**

`**abstrat value space` 만으로 AF또는 RI를 결정하지 않는다. 동일한 abstract type에 대해 여러 `representation` 이 있을 수 있다. 문자 집합은 위와 같이 문자열로 표시되거나 가능한 각 문자에 대해 1비트를 사용하는 비트 벡터로 동일하게 표시될 수 있다.** 

**→ 이 두가지는 매핑하는데 있어서 서로 다른 abstract function이 필요하다.**

**핵심은  rep type을 정의하고 그에 따라 rep에 대한 값의 공간에 대한 값을 선택하더라도 어떤 값이 합법적으로 간주 되는지, 합법적인 값중에 어떻게 해석될지는 결정되지 않는다.**

**문자열에 중복이 없다고 결정하는 대신 중복을 허용하는 동시에 문자가 감소하지 않는 순서로 정렬할 수 있게 precondition을 넣을 수 있다. 이를 통해 문자열에 대해 이진 검색을 수행할 수 있으므로 선형 시간이 아닌 log시간으로 멤버십을 확인 할 수 있다.**

**다른 rep invariant**

```java
public class CharSet {
    private String s;
    // Rep invariant:
    //    s[0] <= s[1] <= ... <= s[s.length()-1]
    // Abstraction Function:
    //   represents the set of characters found in s
    ...
}
```

![http://web.mit.edu/6.005/www/fa15/classes/13-abstraction-functions-rep-invariants/figures/charset-sorted.svg](http://web.mit.edu/6.005/www/fa15/classes/13-abstraction-functions-rep-invariants/figures/charset-sorted.svg)

**동일한 rep value space와 동일한 rep invariant RI를 사용하더라도 서로 다른 `abstract function`(AF)를 사용하여 표현을 다르게 해석 할 수 있다.**

**RI가 모든 문자열을 허용한다고 가정했을 때 위와 같이 AF를 정의하여 배열의 요소를 집합의 요소로 해석할 수 있다. 그러나 Rep가 해석을 결정하도록하는 선언은 없다.**

**연속된 문자 쌍을 하위 범위로 해석하여 문자열 표현 “acgg”가 두 범위 쌍 [ac] 및 [gg]로 해석되어 집합{a,b,c,g}를 나타낼 수 있다. 해당 표현에 대한 AF 및 RI의 모습은 다음과 같다.**

```java
public class CharSet {
    private String s;
    // Rep invariant:
    //    s.length is even
    //    s[0] <= s[1] <= ... <= s[s.length()-1]
    // Abstraction Function:
    //   represents the union of the ranges
    //   {s[i]...s[i+1]} for each adjacent pair of characters 
    //   in s
    ...
}
```

![http://web.mit.edu/6.005/www/fa15/classes/13-abstraction-functions-rep-invariants/figures/charset-sortedrange.svg](http://web.mit.edu/6.005/www/fa15/classes/13-abstraction-functions-rep-invariants/figures/charset-sortedrange.svg)

**중요한 점으로 `abstract type`를 설계한다는 것은 스펙을 위한 `abstract value space`와 구현을 위한 `rep value space`이라는 두 공간을 선택하는 것 뿐만 아니라 사용할 `rep value`와 해석하는 방법을 결정하는 것을 의미한다.**

## Example: Rational Numbers

**다음은 유리수에 대한 abstract date type의 예시이다.**

**rep invariant와 abstraction function를 자세히 살펴보아라.**

```java
public class RatNum {
    private final int numer;
    private final int denom;

    // Rep invariant:
    //   denom > 0
    //   numer/denom is in reduced form

    // Abstraction Function:
    //   represents the rational number numer / denom

    /** Make a new Ratnum == n. */
    public RatNum(int n) {
        numer = n;
        denom = 1;
        checkRep();
    }

    /**
     * Make a new RatNum == (n / d).
     * @param n numerator
     * @param d denominator
     * @throws ArithmeticException if d == 0
     */
    public RatNum(int n, int d) throws ArithmeticException {
        // reduce ratio to lowest terms
        int g = gcd(n, d);
        n = n / g;
        d = d / g;

        // make denominator positive
        if (d < 0) {
            numer = -n;
            denom = -d;
        } else {
            numer = n;
            denom = d;
        }
        checkRep();
    }
}
```

**이 코드에 대한 `abstraction function`과 `representation invariant` 그림이다.**

![http://web.mit.edu/6.005/www/fa15/classes/13-abstraction-functions-rep-invariants/figures/ratnum-af-ri.png](http://web.mit.edu/6.005/www/fa15/classes/13-abstraction-functions-rep-invariants/figures/ratnum-af-ri.png)

`**Reperesentation invariant`에서는 분자/분모 쌍이 기약분수이어야하기 때문에 (2,4) 및 (18,12)와 같은 쌍은 RI외부로 그려야한다.**

**보다 허용적인 RI를 사용하여 동일한 ADT의 또 다른 구현을 설계하는 것이 완전히 합리적일 수 있다. 이러한 변경을 위해서 사용하는 비용이 많이 들 수도 있고 적게 들 수도 있다.**

## Checking the Rep Invariant

**representation invariant는 단지 깔끔한 수학적인 아이디어가 아니다. 구현이 런타임 시 Rep invariant를 주장하는 경우 버그를 조기에 발견 할 수 있다. RatNum의 메서드는 다음과 같다.**

```java
// Check that the rep invariant is true
// *** Warning: this does nothing unless you turn on assertion checking
// by passing -enableassertions to Java
private void checkRep() {
    assert denom > 0;
    assert gcd(numer, denom) == 1;
}
```

**representation을 생성하거나 변경하는 모든 작업(즉, creators(생성자) or mutates(변경자))이 끝날 때마다 `checkRep()`를 호출하여 representation invariant를 주장해야한다. 위의 RatNum 코드를 다시 보면 두 생성자의 끝에서 `checkRep()`를 호출하는 것을 볼 수 있다.**

**Observer 메서드는 일반적으로 checkRep()를 호출할 필요가 없지만 어쨌든 그렇게 하는 것이 좋은 습관이다.**

**→ Observer를 포함한 모든 메서드에서 checkRep()를 호출하면 rep 노출로 인한 rep invariant 위반을 포착할 가능성이 높아진다.**

**checkRep는 왜 비공개인가? rep invariant를 확인하고 시행하는 책임은 구현자체에 있기때문에**

## No Null Values in the Rep

**null값은 문제가 많고 안전하지 않기 때문에 프로그래밍에서 완전히 제거하려고 한다고 한 적 이 있다.**

**6.005에서는 메서드의 precondition과 postcondition에서 객체와 배열이 null이 아니어야 한다는 것을 암시적으로 요구한다.**

**우리는 이러한 금지를 abstract data type의 표현으로 확장한다.** 

```java
class CharSet {
    String s;
}
```

**그러나 해당 representation invariant이 자동으로 포함되므로 `s != null` rep invariant comment에 이를 명시할 필요가 없다.**

**→ checkRep() 메서드에서 여전히 검사를 해야하며 s ≠ null 일 때 올바르게 실패하는지 확인해야 한다. 하지만 다른 rep invariant를 검사하면 null인 경우 예외가 발생하기 때문에 무료로 제공해주는 경우가 많다.**

```java
private void checkRep() {
    assert s.length() % 2 == 0;
    ...
}
```

**만약 s가 null이라면 `s.length()` 에서 null참조가 발생하기 때문에 실패한다.**

**→ 이런식으로 별도로 확인되지 않는 경우 `s!=null` 을 명시해주어야한다.**

---

## ****Documenting the AF, RI, and Safety from Rep Exposure****

**Rep의 private field가 선언된 바로 그 위치에 주석을 사용하여 클래스의 Abstraction fucntion과 Rep Invariant을 문서화하는 것이 좋다.**

**또 다른 좋은 방법은 rep exposure safety argument이다. 이는 표현의 각 부분을 검사하고 표현의 해당 부분을 처리하는 코드를 살펴보고, 코드는 rep를 노출하지 않는다.**

**Rep Invariant, abstraction function 및 rep exposure로 부터 안전한 예시 `Tweet`**

```java
// Immutable type representing a tweet.
public class Tweet {

    private final String author;
    private final String text;
    private final Date timestamp;

    // Rep invariant:
    //   author is a Twitter username (a nonempty string of letters, digits, underscores)
    //   text.length <= 140
    // Abstraction Function:
    //   represents a tweet posted by author, with content text, at time timestamp 
    // Safety from rep exposure:
    //   All fields are private;
    //   author and text are Strings, so are guaranteed immutable;
    //   timestamp is a mutable Date, so Tweet() constructor and getTimestamp() 
    //        make defensive copies to avoid sharing the rep's Date object with clients.

    // Operations (specs and method bodies omitted to save space)
    public Tweet(String author, String text, Date timestamp) { ... }
    public String getAuthor() { ... }
    public String getText() { ... }
    public Date getTimestamp() { ... }
}
```

**타임스탬프에 대한 rep invariant condition이 명시되지 않았다는 것을 알아둬라. (모든 객체 참조에 대해 가지고 있는 `timestamp != null` 이라는 일반적인 가정과는 별도)**

**전체 유형의 `immutability` 속성은 변하지않고 남아있는 모든 필드에 따라 달라지기 때문에 `timestamp` 는 `rep exposure safety argument` 이다.**

`**RatNum` 의 파라미터는 다음과 같다.**

```java
// Immutable type representing a rational number.
public class RatNum {
    private final int numer;
    private final int denom;

    // Rep invariant:
    //   denom > 0
    //   numer/denom is in reduced form, i.e. gcd(|numer|,denom) = 1
    // Abstraction Function:
    //   represents the rational number numer / denom
    // Safety from rep exposure:
    //   All fields are private, and all types in the rep are immutable.

    // Operations (specs and method bodies omitted to save space)
    public RatNum(int n) { ... }
    public RatNum(int n, int d) throws ArithmeticException { ... }
    ...
}
```

`**immutable rep` 는 `rep exposure` 로 부터 안전하게 만들기 쉽다.**

### How to Establish Invarian**ts**

**invariant는 전체 프로그램에 적용되는 속성이다.** 

**불변성을 갖고 있기 위해서 필요한 것**

- 객체의 초기 상태에 invariant는 true
- 객체에 대한 모든 변경 사항이 불변성을 true로 유지하는지 확인

**ADT operation로 해석하면 다음과 같다.**

- creators 그리고 producers는 새로운 객체 인스턴스에 대한 invariant를 설정해야 한다.
- mutators와 observers는 invariant를 유지해야한다.

`**rep exposure` 는 상황을 더 복잡하게 만든다. 만약 rep 가 노출된다면 객체는 ADT 작업뿐만 아니라 프로그램 어느 곳에서나 객체가 변경될 수 있으며 이러한 변경 후에도 invariant가 여전히 유지된다는 것을 보장할 수 없다.** 

**Structural induction :** abstract data type의 invariant이 다음과 같은경우

1. **creators와 producers가 만듬**
2. **mutators와 observers가 유지시킨다.**
3. **representation exposure가 발생하지 않음**

**이러한 조건이 유지된다면 abstract data type의 모든 인스턴스에 invariant가 적용된다.**

---

## ****ADT invariants replace preconditions****

**잘 설계된 abstract data type의 엄청난 장점은 precondition에서 규정해야 하는 속성을 캡슐화하고 적용한다는 것이다.**

**ex) 다음과 같은 스펙 대신 정교한 precondition을 사용한다.**

```java
/** 
 * @param set1 is a sorted set of characters with no repeats
 * @param set2 is likewise
 * @return characters that appear in one set but not the other,
 *  in sorted order with no repeats 
 */
static String exclusiveOr(String set1, String set2);
```

원하는 속성을 캡쳐하는 ADT를 사용할 수 있다.

```java
/** @return characters that appear in one set but not the other */
static SortedSet<Character> exclusiveOr(SortedSet<Character>  set1, SortedSet<Character> set2);
```

ADT의 이름은 프로그래머가 알아야 할 모든 것을 전달하기 때문에 이해하기 쉽다. 또한 Java static checking가 작동하고 필수 조건이 정확히 한 위치인 `SortedSet type`에서 시행될 수 있으므로 버그로부터 더 안전하다.

---

## Summary

- **invariant은 객체의 lifetime 동안 ADT 인스턴스에 대해 항상 적용되는 속성이다.**
- **좋은 ADT는 자체적으로 invariant를 유지한다. creators과 producers가 설정해야하며 observers와 mutators가 보존해야한다.**
- **rep invariant는 유효한 값을 지정하며 런타임시 `checkRep()`를 사용하여 확인해야한다.**
- **abstraction function은 representation을 그것이 나타내는 abstract value에 매핑한다.**
- **rep exposure는 `abstraction function` 과 `invariant preservation` 둘 다 위협한다.**

**SFB :** 좋은 ADT는 자체적으로 invariant를 유지하므로 ADT 클라이언트의 버그에 덜 취약하고 ADT자체 구현 내에서 쉽게 격리될 수 있다. rep invariant을 명시적으로 지정하고 `checkRep()`를 사용하여 런타임에 이를 확인하면 손상된 데이터 구조를 사용하지 않고 fail fast가 가능하다.

**ETU** : Rep invariant과 abstraction function는 데이터의 의미와 그것이 추상화와 어떻게 관련되는지를 설명한다.

**RFC** : abstract data type은 추상화를 구체적인 표현과 분리하므로 클라이언트 코드를 변경하지 않고도 표현을 변경할 수 있다.