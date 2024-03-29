package pl.daveproject.dietapp.caloricneeds.ui;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.daveproject.dietapp.caloricneeds.model.TotalCaloricNeedsDto;
import pl.daveproject.dietapp.caloricneeds.service.TotalCaloricNeedsService;
import pl.daveproject.dietapp.ui.component.Translator;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class TotalCaloricNeedsDataProvider extends
        AbstractBackEndDataProvider<TotalCaloricNeedsDto, TotalCaloricNeedsFilter> implements Translator {
    public static final String ADDED_DATE_SORTING_KEY = "addedDate";
    public static final String WEIGHT_SORTING_KEY = "weight";
    public static final String HEIGHT_SORTING_KEY = "height";
    public static final String AGE_SORTING_KEY = "age";
    public static final String ACTIVITY_LEVEL_SORTING_KEY = "activity-level";
    public static final String VALUE_SORTING_KEY = "value";

    private final TotalCaloricNeedsService totalCaloricNeedsService;

    @Override
    protected Stream<TotalCaloricNeedsDto> fetchFromBackEnd(Query<TotalCaloricNeedsDto, TotalCaloricNeedsFilter> query) {
        var stream = totalCaloricNeedsService.findAll().stream();
        if (query.getFilter().isPresent()) {
            stream = stream.filter(totalCaloricNeeds -> query.getFilter().get().match(totalCaloricNeeds));
        }

        if (query.getSortOrders().size() > 0) {
            stream = stream.sorted(sortComparator(query.getSortOrders()));
        }
        return stream.skip(query.getOffset()).limit(query.getLimit());
    }

    @Override
    protected int sizeInBackEnd(Query<TotalCaloricNeedsDto, TotalCaloricNeedsFilter> query) {
        return (int) fetchFromBackEnd(query).count();
    }

    private Comparator<TotalCaloricNeedsDto> sortComparator(List<QuerySortOrder> sortOrders) {
        return sortOrders.stream()
                .map(sortOrder -> {
                    var comparator = totalCaloricNeedsFieldComparator(sortOrder.getSorted());
                    if (sortOrder.getDirection() == SortDirection.DESCENDING) {
                        comparator = comparator.reversed();
                    }
                    return comparator;
                }).reduce(Comparator::thenComparing).orElse((p1, p2) -> 0);
    }

    private Comparator<TotalCaloricNeedsDto> totalCaloricNeedsFieldComparator(String sorted) {
        return switch (sorted) {
            case WEIGHT_SORTING_KEY -> Comparator.comparing(TotalCaloricNeedsDto::getWeight);
            case HEIGHT_SORTING_KEY -> Comparator.comparing(TotalCaloricNeedsDto::getHeight);
            case VALUE_SORTING_KEY -> Comparator.comparing(TotalCaloricNeedsDto::getValue);
            case ADDED_DATE_SORTING_KEY -> Comparator.comparing(TotalCaloricNeedsDto::getAddedDate);
            case AGE_SORTING_KEY -> Comparator.comparing(TotalCaloricNeedsDto::getAge);
            case ACTIVITY_LEVEL_SORTING_KEY ->
                    Comparator.comparing(totalCaloricNeeds -> getTranslation(totalCaloricNeeds.getActivityLevel().getTranslationKey()));
            default -> (p1, p2) -> 0;
        };
    }
}
