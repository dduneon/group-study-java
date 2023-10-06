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