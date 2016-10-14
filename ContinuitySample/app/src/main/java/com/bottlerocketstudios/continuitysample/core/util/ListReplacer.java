package com.bottlerocketstudios.continuitysample.core.util;

import java.util.List;

/**
 * Created on 9/14/16.
 */
public class ListReplacer {

    /**
     * Transforms all of the elements from sourceList to destinationList using the provided transform.
     */
    public static <S, D> void selectiveReplace(List<S> sourceList, List<D> destinationList, LrTransform<S, D> lrTransform) {
        int destinationListSize = destinationList.size();
        for (int i = 0; i < sourceList.size(); i++) {
            if (i < destinationListSize) {
                S source = sourceList.get(i);
                D destination = destinationList.get(i);
                if (lrTransform.matches(source, destination)) {
                    if (lrTransform.shouldReplace(source, destination)) {
                        destinationList.set(i, lrTransform.transform(source));
                    }
                } else {
                    destinationList.set(i, lrTransform.transform(source));
                }
            } else {
                destinationList.add(lrTransform.transform(sourceList.get(i)));
            }
        }

        if (destinationList.size() > sourceList.size()) {
            destinationList.subList(sourceList.size(), destinationList.size()).clear();
        }
    }

    public interface LrTransform<S, D> {
        boolean matches(S source, D destination);
        boolean shouldReplace(S source, D destination);
        D transform(S source);
    }
}
