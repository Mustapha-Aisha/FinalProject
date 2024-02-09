package com.FinalProject.NextGenFinalProject.Entity;

public enum Category {
        BEER("Beer"),
        WINE("Wine"),
        WHISKEY("Whiskey"),
        VODKA("Vodka"),
        RUM("Rum"),
        TEQUILA("Tequila"),
        GIN("Gin"),
        COCKTAIL("Cocktail"),
        OTHER("Other");

        private final String displayName;

        Category(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }


