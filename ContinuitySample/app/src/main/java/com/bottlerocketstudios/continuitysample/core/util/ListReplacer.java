package com.bottlerocketstudios.continuitysample.core.util;

import java.util.List;

/**
 * Process a list using the supplied LrTransform. The selectiveReplace method will visit each node
 * on two lists of different types.
 * <ol>
 *   <li>While the lists match, skip entries in the destination</li>
 *   <li>If a change is made for a matching entry, replace it.</li>
 *   <li>If a mismatch is found, replace the item in the destination.</li>
 *   <li>If items in the source outnumber the items in the destination, append to destination</li>
 *   <li>If items in the destination outnumber items in the source, truncate the destination.</li>
 * </ol>
 * This runs in a single pass to be O(n). If an item is inserted in the source, it will cause recreation
 * of all items following it in the destination. Recreation should be inexpensive (MapStruct).
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
