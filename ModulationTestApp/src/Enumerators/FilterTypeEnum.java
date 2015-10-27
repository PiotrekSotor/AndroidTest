package Enumerators;

/**
 * Created by Piotrek on 2015-10-21.
 */

public enum FilterTypeEnum {
    /**
     * BlurFilter - rozmywający, parametr wpływa na szerokość rozmycia
     * ScaleFilter - przesunięcie częstotliwości, parametr wpływa na stopień przesunięcia
     * CapacityFilter - przepustowy, parametrem jest zbiór punktów rysujących krzywą filtru x: (0,f/2), y: (0,1)
     */
    BlurFilter,
    ScaleFilter,
    CapacityFilter;
}
