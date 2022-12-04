
package com.currency.money;

import java.util.List;

import com.currency.money.Classifier.Recognition;

public interface ResultsView {
  public void setResults(final List<Recognition> results);
}
