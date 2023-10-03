# Debugging

## Objectives

**수업 주제는 체계적 디버깅이다.**

**때때로 디버깅할 수 밖에 없는 경우가 있다. 특히 전체 시스템을 연결했을 때만 버그가 발견되거나 시스템이 배포된 후 사용자가 보고하는 경우에는 특정 모듈로 localize 하기 어려울 수 있다. 이러한 상황에서는 보다 효과적인 디버깅을 위한 전략을 제안할 수 있다.**

---

## ****Reproduce the Bug****

- **실패를 일으키는 작고 반복 가능한 테스트 케이스를 찾는 것 부터 시작하는 것이 좋다.**
- **만약 regression testing에서 버그를 발견했다면 운이 좋은 것이다.**
- **실패한 테스트 케이스는 이미 test suite에 있다.**

**만약 유저에 의해서 버그가 보고된거라면 재현하기 힘들 수 있다.** 

**(혹은 GUI혹은 다중 스레드 프로그램의 경우 이벤트 타이밍이나 스레드 실행에 따라 재현하기 힘들 수 있다.)**

**그럼에도 불구하고 테스트 케이스를 작고 반복 가능하게 만들기 위해서 들이는 모든 노력은 보상을 받을 것이다.**

**→ 버그를 검색하고 고치는 동안 계속해서 테스트 케이스를 실행해야 하기 때문에** 

**→ 또한 big fix 이후에는 `regression test suite`에 `test case` 를 추가하여 버그가 다시는 발생하지 않도록 할 수 있다.**

버그에 대한 테스트 케이스가 있으면 이 테스트 작업을 수행하는 것이 목표가 된다.

ex)

```java
/**
 * Find the most common word in a string.
 * @param text string containing zero or more words, where a word
 *     is a string of alphanumeric characters bounded by nonalphanumerics.
 * @return a word that occurs maximally often in text, ignoring alphabetic case.
 */
public static String mostCommonWord(String text) {
    ...
}
```

**사용자가 셰익스피어 희곡의 전체 테스트를 `mostCommonWord(allShakespearesPlaysConcatenated)` 와 같은 메서드에 전달하고 `"the"` 와 `“a”` 와 같은 영어단어를 반환하는 대신 예상치 못한 값을 반환하는 것(ex: `”e”`)으로  반환한다는 사실을 발견했다.**

**셰익스피어 희곡에는 800,000개 이상의 단어가 포함된 100,000줄이 있으므로 이 입력은 프린트 디버깅 및 중단점 디버깅과 같은 일반적인 방법으로는 디버깅하기가 매우 어려울 것이다.**

**버그가 있는 입력의 크기를 여전히 동일한(또는 매우 유사한) 버그를 나타내는 관리 가능한 크기로 줄이는 작업을 먼저 수행하면 디버깅이 쉬워진다.**

- 셰익스피어 전반부에도 같은 버그가 나타날까?(이진검색을 써봐라)
- single play에도 동일한 버그가 있을까?
- single speech에도 동일한 버그가 있을까?

**작은 테스트 케이스를 찾았다면 해당 테스트 케이스를 사용하여 버그를 찾아 수정한 다음 원래 버그 입력으로 돌아가서 동일한 버그를 수정했는지 확인해라.**

---

## ****Understand the Location and Cause of the Bug****

버그와 그 원인을 현지화 하려면 과학적인 방법을 사용할 수 있다.

1. **Study the data :** 버그를 일으키는 테스트 입력과 잘못된 결과, 실패한 어설션 및 그로 인해 발생하는 스택 트레이스를 살펴봐라.
2. **Hypothesize :** 버그가 있을 수 있는 위치와 있을 수 없는 위치에 대해 모든 데이터와 일치하는 가설을 제안한다. 처음에는 이 가설을 일반화하는 것이 좋다.
3. **Experiment :** 가설을 테스트하는 실험을 생각해봐라. 처음에는 실험을 관찰로 만드는 것이 좋다. 즉 정보를 수집하지만 시스템을 가능한 적게 방해하는 증명이다.
4. **Repeat :** 실험을 통해 수집한 데이터를 이전에 알고 있던 데이터에 추가하고 새로운 가설을 세워봐라. 몇가지 가능성을 배제하고 가능한 위치와 버그 원인의 범위를 좁혔어야한다.

`**mostConnonWord()` 3가지 helper method를 사용하여 구체화된 예제 예시**

```java
/**
 * Find the most common word in a string.
 * @param text string containing zero or more words, 
 *     where a word is a string of alphanumeric 
 *     characters bounded by nonalphanumerics.
 * @return a word that occurs maximally often in text, 
 *         ignoring alphabetic case.
 */
public static String mostCommonWord(String text) {
    ... words = splitIntoWords(text); ...
    ... frequencies = countOccurrences(words); ...
    ... winner = findMostCommon(frequencies); ...
    ... return winner;
}

/** Split a string into words ... */
private static List<String> splitIntoWords(String text) {
    ...
}

/** Count how many times each word appears ... */
private static Map<String,Integer> countOccurrences(List<String> words) {
    ...
}

/** Find the word with the highest frequency count ... */
private static String findMostCommon(Map<String,Integer> frequencies) {
    ...
}
```

1. **Study the Data**

데이터의 중요한 형태 중 하나는 예외의 스택 트레이스이다. 스택 트레이스를 읽는 연습을 해라. 왜냐하면 버그가 어디에 있고 무엇인지에 대한 엄청난 양의 정보를 제공한다.

작은 테스트 케이스를 분리하는 과정을 통해 이전에 갖고 있지 않았던 데이터를 얻을 수 있다. 하나는 성공하고 하나는 실패한다는 의미에서 버그를 포함하는 두개의 관련 테스트 케이스가 있을 수 있다. 

ex) `mostCommonWords("c c, b")` 는 오류가 발생 할 수 있지만 `mostCommonWords("c c b")` 는 괜찮다.

1. ****Hypothesize****

프로그램을 모듈이나 알고리즘의 단계로 생각하고 프로그램의 전체 섹션을 한 번에 배제하는 것이 도움된다.

![http://web.mit.edu/6.005/www/fa15/classes/11-debugging/figures/dataflow.png](http://web.mit.edu/6.005/www/fa15/classes/11-debugging/figures/dataflow.png)

데이터의 흐름은 `mostCommonWord()`오른쪽에 표시된다. 버그 증상이 `countOccurrences()` 에서 발생한 경우 다운 스트림의 모든 항목 특히 `findMostFrequent()`는 배제하고 생각할 수 있다.

그런 다음 버그를 더욱 자세히 파악하려는 가설을 선택한다. 버그가 `splitIntoWords()` 에 있어서 결과가 손상되어 `countOccurrences()` 에서 예외가 발생한다고 생각 할 수 있다.

그런 다음 실험을 사용해서 해당 가설을 테스트 한다. 가설이 참이라면 `countOccurrences()`

는 문제의 원인에서 제외시킨다. 만약 거짓이라면 `splitIntoWords()`를 제외시킨다.

1. ****Experiment****

좋은 실험은 시스템을 크게 방해하지 않고 시스템을 부드럽게 관찰하는 것이다.

- 다른 테스트 케이스를 실행해봐라. 위에서 설명한 테스트 케이스 축소 프로세스에서는 테스트 케이스를 실험으로 사용했다.
- 실행중인 프로그램에 print문이나 assertion을 삽입하여 내부 상태를 확인해라
- 디버거를 사용하여 중단점을 설정한 다음 코드를 한단계씩 실행하고 변수 및 개체 값을 살펴봐라

단순한 조사 대신 가정된 버그에 대한 수정 사항을 삽입하려는 유혹이 있다. 이것은 거의 항상 잘못된 일이다. 

1. 일종의 추측 및 테스트 프로그래밍으로 이어지면서 스파게티 코드가 된다.
2. 수정 사항은 실제로 버그를 제거하지 않고 실제 버그를 가릴 수 있다.

**예를 들어 `ArrayOutOfBoundsException` 이 일어난 경우 무슨 일이 일어나는지 이해하려고 노력해라. 실제로 문제를 해결하지 않고 예외를 피하거나 잡는 코드를 추가하지마라.**

---

## Other tip

- **************이진 검색을 통한 현지화 :************** 디버깅은 검색 프로세스이므로 때로는 이진 검색을 사용하여 프로섹스 속도를 높일 수 있다. 예를 들어 `mostCommonWords` 에서 데이터는 세가지 helper method를 통해 흐르는다. 이진 검색을 수행하려면 데이터를 반으로 나누고 첫 번째 헬퍼 메서드 호출과 두번째 헬퍼 메서드 호출 사이 어딘가에서 버그가 발견된다고 추측하고 여기에 프로브(중단점, 프린트문, 어설션문)를 삽입하여 결과를 확인한다.

- ****************************************************************가설의 우선순위를 정해라**************************************************************** : 가설을 세울 때 시스템의 각 부분마다 실패 가능성이 다르다는 점을 염두에 두는 것이 좋다. 예를 들어 오래되고 잘 테스트된 코드는 최근에 추가된 코드보다 더 신뢰할 수 있다. Java 라이브러리 코드는 아마도 우리의 코드보다 더 신뢰할 수 있다.

- **********************************************************구성요소를 교환해라 :********************************************************** 동일한 인터페이스를 만족하는 모듈의 다른 구현이 있고 해당 모듈이 의심되는 경우 수행할 수 있는 한 가지 실험은 대안으로 교체해 보는 것이다. 예를 들어 `BinarySearch()` 구현이 의심된다면 대신 더 간단한 선형 검색을 사용해라. java.util.ArrayList가 의심되는 경우 java,util.LinkedList로 교체할 수 있다. 대신 구성 요소를 의심할 타당한 이유가 없는 한 이 작업을 수행하지 마라

- **소스 코드와 개체 코드가 최신인지 확인해라 :** 저장소에서 최신 버전을 가져와 모든 바이너리 파일을 삭제하고 모든 것을 컴파일해라.

- **도움을 받아라 :** 대화 상대가 무슨 말을 하는지 전혀 모르는 경우에도 다른 사람에게 문제를 설명하는 것이 도움이 되는 경우가 많다.
- **자라 :** 너무 피곤하면 디버깅 잘 못하니까 효율성을 위해서 지연 시간을 교환해라

---

## ****Fix the Bug****

**버그를 발견하고 그 원인을 이해했다면 세 번째 단계는 버그 수정을 고안하는 것입니다. 패치를 적용하고 계속 진행하려는 유혹을 피해라. 버그가 철자가 틀린 변수나 메소드 매개변수와 같은 코딩 오류인지, 아니면 제대로 지정되지 않았거나 불충분한 인터페이스와 같은 설계 오류인지 확인해라.**

**버그에 관련된 다른 버그가 있는지도 확인해봐라. 0으로 나누기 오류가 발견된 경우 코드의 다른 곳에서도 해당 오류를 발견 할 수 있다. 앞으로 이와 같은 버그가 발생하지 않도록 코드를 안전하게 만들어라. 또한 수정 사항이 어떤 영향을 미칠지도 고려해라(다른 코드가 손상되면 안됨)**

**마지막으로 수정 사항을 적용한 후 `regression test suite`에 `test case` 를 추가하고 모든 테스트를 실행하여 (a)버그가 수정되엇는지, (b) 새로운 버그가 발생하지 않았는지 확인해라.**

---

## Summary

체계적으로 디버깅하는 방법을 살펴보았다.

- 버그를 테스트 케이스로 재현하고 regression suite에 넣는다.
- 과학적인 방법으로 버그를 찾아봐라
- 성급하게 생각하지 말고 신중하게 버그를 수정해라

**SFB** : 버그를 예방하고 제거하려고 노력한다.

**ETU :** 정적 타이핑, 파이널 선언, 어설션과 같은 기술은 코드의 가정에 대한 추가 문서이다. 변수 범위를 최소화하면 살펴볼 코드가 줄어들기 때문에 독자가 변수가 사용되는 방식을 더 쉽게 이해할 수 있다.

**RFC** : 어설션과 정적 타이핑은 가정을 자동으로 확인 가능한 방식으로 문서화하므로 미래의 프로그래머가 코드를 변경할 때 우발적인 가정 위반이 감지된다.