package com.twbraam.twittercurator.kafkaflink.processor

import com.twbraam.twittercurator.utils.model.{FreshTweet, StaleTweet}
import org.apache.flink.api.common.state.{ListState, ListStateDescriptor}
import org.apache.flink.api.common.typeinfo.{TypeHint, TypeInformation}
import org.apache.flink.runtime.state.{FunctionInitializationContext, FunctionSnapshotContext}
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.util.Collector

import scala.collection.mutable.ListBuffer


class TweetRefresher(threshold: Int = 100)
  extends ProcessFunction[StaleTweet, FreshTweet]
    with CheckpointedFunction {


  private var checkpointedState: ListState[StaleTweet] = _

  private val bufferedElements = ListBuffer[StaleTweet]()

  def processElement(
                      element: StaleTweet,
                      context: ProcessFunction[StaleTweet, FreshTweet]#Context,
                      collector: Collector[FreshTweet]): Unit = {

    bufferedElements += element
    if (bufferedElements.size == threshold) {
      for (element <- bufferedElements) {


      }
      bufferedElements.clear()
    }
  }

  override def snapshotState(context: FunctionSnapshotContext): Unit = {
    checkpointedState.clear()
    for (element <- bufferedElements) {
      checkpointedState.add(element)
    }
  }

  override def initializeState(context: FunctionInitializationContext): Unit = {
    val descriptor = new ListStateDescriptor[StaleTweet](
      "buffered-elements",
      TypeInformation.of(new TypeHint[StaleTweet]() {})
    )

    checkpointedState = context.getOperatorStateStore.getListState(descriptor)

    if(context.isRestored) {
      checkpointedState.get().forEach(element =>
        bufferedElements += element)
    }
  }

}

