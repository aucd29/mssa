package com.example.mssa.model.local.meetingroom

import brigitte.IRecyclerDiff

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-12-21 <p/>
 */

data class MeetingRoom(
    val name: String,
    val location: String,
    val reservations: List<ReservationTime>
)

data class ReservationTime(
    val startTime: String,
    val endTime: String
)

data class AvailableMeetingRoom(
    val id: Int,
    val name: String
): IRecyclerDiff {
    override fun itemSame(item: IRecyclerDiff) =
        id == (item as AvailableMeetingRoom).id

    override fun contentsSame(item: IRecyclerDiff) =
        name == (item as AvailableMeetingRoom).name
}

data class ReservationInfo(
    val id: Int,
    val name: String,
    val location: String
): IRecyclerDiff {
    override fun itemSame(item: IRecyclerDiff) =
        id == (item as ReservationInfo).id

    override fun contentsSame(item: IRecyclerDiff) =
        name == (item as ReservationInfo).name
}