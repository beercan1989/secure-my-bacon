/*
 * Copyright 2016 James Bacon
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

package uk.co.baconi.secure.api.password;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@ToString
@AllArgsConstructor
public class NewPassword {

    @Valid
    @NotNull
    private final NewPasswordBag bag;

    @Valid
    @NotNull
    private final NewPasswordPassword password;

    @Getter
    @ToString
    @AllArgsConstructor
    static class NewPasswordBag {

        @NotBlank
        private final String name;
    }

    @Getter
    @ToString(exclude = "password")
    @AllArgsConstructor
    static class NewPasswordPassword {

        private final String whereFor;
        private final String username;

        @NotBlank
        private final String password;
    }
}
