package domain;

import dto.RankAnalysisDTO;
import dto.RankResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RankAnalysis {
    public static final List<Rank> ANALYZED_RANKS = Arrays.asList(
            Rank.FIFTH,
            Rank.FOURTH,
            Rank.THIRD,
            Rank.SECOND,
            Rank.FIRST);

    private final Counter<Rank> counter;

    private RankAnalysis(Counter<Rank> counter) {
        // 빙어적 복사
        // Todo: 어디서 이 역할을 책임지는게 좋을까?
        this.counter = Counter.newInstance(counter);
    }

    public static RankAnalysis from(Counter<Rank> counter) {
        return new RankAnalysis(counter);
    }

    public int count(Rank rank) {
        return counter.count(rank);
    }

    public double getEarningRate() {
        long totalWinningMoney = 0;
        long totalUsedCnt = 0;
        for (Rank rank : counter.keySet()) {
            int cnt = count(rank);
            totalWinningMoney += cnt * (long) rank.getWinningMoney();
            totalUsedCnt += cnt;
        }

        return (totalUsedCnt == 0) ? 0.0 : totalWinningMoney / (double) (totalUsedCnt * Lotto.PRICE.toInt());
    }

    public RankAnalysisDTO toDTO() {
        List<RankResult> results = new ArrayList<>();
        long reward = 0;
        for (Rank rank : ANALYZED_RANKS) {
            results.add(RankResult.of(rank, count(rank)));
            reward += count(rank) * (long) rank.getWinningMoney();
        }
        return RankAnalysisDTO.of(results, reward, getEarningRate());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RankAnalysis that = (RankAnalysis) o;
        return Objects.equals(counter, that.counter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(counter);
    }
}
