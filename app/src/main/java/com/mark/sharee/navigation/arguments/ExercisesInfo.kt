package com.mark.sharee.navigation.arguments

import com.mark.sharee.navigation.Arguments
import com.mark.sharee.network.model.responses.ExerciseCategory
import com.mark.sharee.network.model.responses.GeneralPollResponse
import com.mark.sharee.network.model.responses.Message

data class ExercisesInfo(val exerciseCategory: ExerciseCategory): Arguments()