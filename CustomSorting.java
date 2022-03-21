
package me.paperclip.paperclipflipper;

import gg.essential.vigilance.data.PropertyData;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import gg.essential.vigilance.data.Category;
import java.util.Comparator;
import gg.essential.vigilance.data.SortingBehavior;

class CustomSorting extends SortingBehavior
{
    @NotNull
    public Comparator<? super Category> getCategoryComparator() {
        return (o1, o2) -> 0;
    }
    
    @NotNull
    public Comparator<? super Map.Entry<String, ? extends List<PropertyData>>> getSubcategoryComparator() {
        return (o1, o2) -> 0;
    }
    
    @NotNull
    public Comparator<? super PropertyData> getPropertyComparator() {
        return (o1, o2) -> 0;
    }
}
