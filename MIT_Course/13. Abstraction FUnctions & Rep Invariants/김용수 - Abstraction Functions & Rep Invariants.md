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

**ADT를 생각할 때 두 값 공간간의 관계를 고려하는 것이 도움이 된다.**

**Rep 값의 공간은 실제 구현 엔티티의 값으로 구성된다. 간단한 경우에는 추상 유형이 단일 객체로 구현되지만 실제로는 복잡하다.**

**문자 집합을 표현하기 위해서 문자열을 사용한다고 가정**

```java
public class CharSet {
    private String s;
    ...
}
```

`representation space` R에는 문자열이 포함되고 `abstract space` A에는 수학적으로 표현된 문자 집합이 포함된다. Rep 값이 나타내는 추상 값까지 괄호를 사용해서 표시 할 수 있다.

![http://web.mit.edu/6.005/www/fa15/classes/13-abstraction-functions-rep-invariants/figures/charset-af-ri.png](http://web.mit.edu/6.005/www/fa15/classes/13-abstraction-functions-rep-invariants/figures/charset-af-ri.png)

- **모든 abstract value는 rep value로 매핑 :** abstract type를 구현하는 목적은 abstract값에 대한 작업을 지원하는 것이다. 그렇다면 우리는 가능한 모든 abstract value를 생성 하고 조작 할 수 있어야 하며, 따라서 그 값은 표현 가능해야 한다.
- **몇개의 abstract value는 둘 이상의 rep value에 매핑됨 :** 이는 rep가 엄격한 인코딩이기 때문이다. 순서가 지정되지 않는 문자 집합을 문자열로 표현하는 방법은 여러가지가 있다.
- **모든 rep value가 매핑되는 것은 아님 :** 이 경우 문자열 “abbc”는 매핑되지 않는다. 문자열에 중복된 내용이 포함되서는 안된다고 결정했다. 이렇게 하면 특정 문자의 첫 번째 인스턴스에 도달할 때 제거 메소드를 종료할 수 있다. 최대 한 개가 있을 수 있다는 것을 알고 있다.

실제로 우리는 두 공간의 몇가지 요소와 관계만 설명할 수 있다. 그래프 전체는 무한하다. 그래서 두가지로 설명한다.

1. **rep value을 대표하는 abstract value에 매핑하는 abstract function**

> AF : R → A
> 

이해 안감 

1. rep value을 boolean으로 매핑하는 rep invariant

> RI : R → boolean
> 

이해 안감 

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