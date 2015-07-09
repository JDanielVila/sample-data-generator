/*
 * Copyright 2014 Open mHealth
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openmhealth.data.generator.domain;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.security.SecureRandom;
import java.util.Random;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;


/**
 * A random variable that models normally-distributed real values. If limits are specified, returned values will
 * fall between the specified bounds.
 *
 * @author Emerson Farrugia
 */
public class BoundedRandomVariable {

    private static final Random prng = new SecureRandom();

    private Double variance = 0.0;
    private Double standardDeviation = 0.0;
    private Double minimumValue;
    private Double maximumValue;


    public BoundedRandomVariable() {
    }

    public BoundedRandomVariable(Double standardDeviation) {

        checkNotNull(standardDeviation);
        setStandardDeviation(standardDeviation);
    }

    public BoundedRandomVariable(Double standardDeviation, Double minimumValue, Double maximumValue) {

        checkNotNull(standardDeviation);
        checkNotNull(minimumValue);
        checkNotNull(maximumValue);
        checkArgument(minimumValue <= maximumValue);

        setStandardDeviation(standardDeviation);
        setMinimumValue(minimumValue);
        setMaximumValue(maximumValue);
    }

    @NotNull
    @Min(0)
    public Double getVariance() {
        return variance;
    }

    public void setVariance(Double variance) {

        checkNotNull(variance);
        checkArgument(variance >= 0);

        this.variance = variance;
        this.standardDeviation = Math.sqrt(variance);
    }

    @NotNull
    @Min(0)
    public Double getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(Double standardDeviation) {

        checkNotNull(standardDeviation);
        checkArgument(standardDeviation >= 0);

        this.standardDeviation = standardDeviation;
        this.variance = Math.pow(standardDeviation, 2.0);
    }

    /**
     * @return a lower bound on the values that can be generated
     */
    public Double getMinimumValue() {
        return minimumValue;
    }

    public void setMinimumValue(Double minimumValue) {

        checkArgument(minimumValue == null || maximumValue == null || minimumValue <= maximumValue);

        this.minimumValue = minimumValue;
    }

    /**
     * @return an upper bound on the values that can be generated
     */
    public Double getMaximumValue() {
        return maximumValue;
    }

    public void setMaximumValue(Double maximumValue) {

        checkArgument(minimumValue == null || maximumValue == null || minimumValue <= maximumValue);

        this.maximumValue = maximumValue;
    }

    /**
     * @param mean the mean of the random variable
     * @return the next value generated by the random variable
     */
    public Double nextValue(Double mean) {

        Double nextValue;

        do {
            nextValue = prng.nextGaussian() * standardDeviation + mean;

            if ((minimumValue == null || nextValue >= minimumValue) &&
                    (maximumValue == null || nextValue <= maximumValue)) {
                break;
            }
        }
        while (true);

        return nextValue;
    }

    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder("BoundedRandomVariable{");

        sb.append("variance=").append(variance);
        sb.append(", standardDeviation=").append(standardDeviation);
        sb.append(", minimumValue=").append(minimumValue);
        sb.append(", maximumValue=").append(maximumValue);
        sb.append('}');

        return sb.toString();
    }
}