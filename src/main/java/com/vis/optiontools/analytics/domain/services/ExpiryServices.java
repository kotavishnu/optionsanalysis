package com.vis.optiontools.analytics.domain.services;

import java.util.Set;

import com.vis.optiontools.analytics.domain.ExpiryDirectionPrediction;
import com.vis.optiontools.analytics.domain.Strike;

public interface ExpiryServices {
	public Set<Strike> getExpiryGetProbableStrikes();
	public ExpiryDirectionPrediction predictDirection();
}
