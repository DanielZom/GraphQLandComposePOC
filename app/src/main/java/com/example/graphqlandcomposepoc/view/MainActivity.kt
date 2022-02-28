package com.example.graphqlandcomposepoc.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.example.graphqlandcomposepoc.LaunchesPastQuery
import com.example.graphqlandcomposepoc.R
import com.example.graphqlandcomposepoc.view.theme.GraphQLAndComposePOCTheme
import com.example.graphqlandcomposepoc.view.theme.SpaceXBlue
import com.example.graphqlandcomposepoc.view.theme.White
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getPastLaunchesData()
        setContent {
            val navController = rememberNavController()
            GraphQLAndComposePOCTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Image(
                                        painter = painterResource(id = R.drawable.logo),
                                        null
                                    )
                                },
                                elevation = 8.dp,
                                backgroundColor = MaterialTheme.colors.background
                            )
                        }) {
                        NavHost(
                            navController = navController,
                            startDestination = "list"
                        ) {
                            composable("list", arguments = listOf(
                                navArgument("mission") { type = NavType.StringType }
                            )) {
                                MissionListScreen(viewModel.state) { mission ->
                                    var missionJson = viewModel.launchJsonAdapter.toJson(mission)
                                    if (missionJson.contains("&")) {
                                        missionJson = missionJson.replace("&", "")
                                    }
                                    navController.navigate("detail?mission=$missionJson")
                                }
                            }
                            composable("detail?mission={mission}") { backStackEntry ->
                                backStackEntry.arguments?.getString("mission")?.let {
                                    MissionDetailScreen(
                                        mission = viewModel.launchJsonAdapter.fromJson(it)!!
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MissionListScreen(
    state: State<List<LaunchesPastQuery.LaunchesPast?>?>,
    onClick: (LaunchesPastQuery.LaunchesPast) -> Unit
) {
    state.value?.let {
        val marginExtraSmall = 4.dp
        val marginSmall = 8.dp
        val marginNormal = 12.dp
        val cardHeight = 150.dp
        val defaultTitle = stringResource(id = R.string.default_title)
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            contentPadding = PaddingValues(top = marginNormal)
        ) {
            items(it, itemContent = { item ->
                item?.let {
                    Card(
                        elevation = marginNormal,
                        shape = RoundedCornerShape(marginNormal),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                bottom = marginNormal,
                                start = marginNormal,
                                end = marginNormal
                            )
                            .clickable {
                                onClick.invoke(item)
                            },
                        backgroundColor = SpaceXBlue
                    ) {
                        ConstraintLayout {
                            val (data, image) = createRefs()
                            Column(
                                modifier = Modifier
                                    .constrainAs(data) {
                                        top.linkTo(parent.top, margin = marginSmall)
                                        linkTo(
                                            start = parent.start, end = image.start,
                                            startMargin = marginSmall, endMargin = marginExtraSmall
                                        )
                                        width = Dimension.fillToConstraints
                                    }) {
                                FlowRow {
                                    Text(
                                        buildAnnotatedString {
                                            withStyle(MaterialTheme.typography.body1.toSpanStyle()) {
                                                append(stringResource(id = R.string.mission_name_title))
                                            }
                                            append(" ${item.mission_name ?: defaultTitle}")
                                        },
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.body2
                                    )
                                }
                                Row {
                                    Text(
                                        buildAnnotatedString {
                                            withStyle(MaterialTheme.typography.body1.toSpanStyle()) {
                                                append(stringResource(id = R.string.mission_date_title))
                                            }
                                            append(" ${item.getFormattedLaunchDate()}")
                                        },
                                        style = MaterialTheme.typography.body2
                                    )
                                }
                                Row {
                                    Text(
                                        buildAnnotatedString {
                                            withStyle(MaterialTheme.typography.body1.toSpanStyle()) {
                                                append(stringResource(id = R.string.rocket_name_title))
                                            }
                                            append(" ${item.rocket?.rocket_name ?: defaultTitle}")
                                        },
                                        style = MaterialTheme.typography.body2
                                    )
                                }
                                FlowRow {
                                    Text(
                                        buildAnnotatedString {
                                            withStyle(MaterialTheme.typography.body1.toSpanStyle()) {
                                                append(stringResource(id = R.string.launch_site_title))
                                            }
                                            append(" ${item.launch_site?.site_name ?: defaultTitle}")
                                        },
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.body2
                                    )
                                }
                            }
                            val images = item.links?.flickr_images
                            Image(
                                painter = rememberImagePainter(
                                    ImageRequest.Builder(
                                        LocalContext.current
                                    ).data(if (images.isNullOrEmpty()) null else images.first())
                                        .transformations(
                                            RoundedCornersTransformation(
                                                topRight = marginNormal.value,
                                                bottomRight = marginNormal.value
                                            )
                                        )
                                        .fallback(R.drawable.no_rocket_image).build()
                                ), null, modifier = Modifier
                                    .size(cardHeight)
                                    .constrainAs(image) {
                                        end.linkTo(parent.end)
                                        top.linkTo(parent.top)
                                        bottom.linkTo(parent.bottom)
                                    }
                            )
                        }
                    }
                }
            })
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MissionDetailScreen(mission: LaunchesPastQuery.LaunchesPast) {
    if (!mission.ships.isNullOrEmpty()) {
        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }
        val launch = stringResource(id = R.string.launch)
        val rocketLaunchSate = remember { mutableStateOf(launch) }
        rocketLaunchSate.value
        HorizontalPager(
            count = mission.ships.size,
            modifier = Modifier
                .background(SpaceXBlue)
                .fillMaxHeight()
        ) { i ->
            ConstraintLayout(modifier = Modifier.fillMaxHeight()) {
                val defaultTitle = stringResource(id = R.string.default_title)
                val noData = stringResource(id = R.string.no_data)
                val (image, data, button, snackBar) = createRefs()
                val ship = mission.ships[i]!!
                Image(
                    painter = rememberImagePainter(ship.image!!),
                    null,
                    modifier = Modifier
                        .constrainAs(image) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .height(300.dp),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                )
                Column(modifier = Modifier.constrainAs(data) {
                    top.linkTo(image.bottom, margin = 8.dp)
                    linkTo(
                        start = parent.start,
                        end = parent.end,
                        bias = 0F,
                        startMargin = 8.dp,
                        endMargin = 8.dp
                    )
                }) {
                    FlowRow {
                        Text(
                            buildAnnotatedString {
                                withStyle(MaterialTheme.typography.body1.toSpanStyle()) {
                                    append(stringResource(id = R.string.ship_name_title))
                                }
                                append(" ${ship.name ?: defaultTitle}")
                            },
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.body2
                        )
                    }
                    FlowRow {
                        Text(
                            buildAnnotatedString {
                                withStyle(MaterialTheme.typography.body1.toSpanStyle()) {
                                    append(stringResource(id = R.string.ship_home_port_title))
                                }
                                append(" ${ship.home_port ?: defaultTitle}")
                            },
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.body2
                        )
                    }
                    Row {
                        Text(
                            buildAnnotatedString {
                                withStyle(MaterialTheme.typography.body1.toSpanStyle()) {
                                    append(stringResource(id = R.string.ship_landings_title))
                                }
                                append(" ${ship.successful_landings ?: noData}")
                            },
                            style = MaterialTheme.typography.body2
                        )
                    }
                    Row {
                        Text(
                            buildAnnotatedString {
                                withStyle(MaterialTheme.typography.body1.toSpanStyle()) {
                                    append(stringResource(id = R.string.ship_weight_title))
                                }
                                if (ship.weight_kg != null) append(" ${ship.weight_kg} kg") else append(
                                    " $noData"
                                )
                            },
                            style = MaterialTheme.typography.body2
                        )
                    }
                    Row {
                        Text(
                            buildAnnotatedString {
                                withStyle(MaterialTheme.typography.body1.toSpanStyle()) {
                                    append(stringResource(id = R.string.ship_age_title))
                                }
                                append(" ${ship.year_built ?: noData}")
                            },
                            style = MaterialTheme.typography.body2
                        )
                    }
                }
                Button(onClick = {
                    scope.launch {
                        rocketLaunchSate.value = "3"
                        delay(1000)
                        rocketLaunchSate.value = "2"
                        delay(1000)
                        rocketLaunchSate.value = "1"
                        delay(1000)
                        snackbarHostState.showSnackbar("Just kidding :)", null)
                        rocketLaunchSate.value = launch
                    }
                }, modifier = Modifier.constrainAs(button) {
                    bottom.linkTo(parent.bottom, margin = 8.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }, colors = ButtonDefaults.buttonColors(backgroundColor = White)) {
                    Text(text = rocketLaunchSate.value, color = SpaceXBlue)
                }
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.constrainAs(snackBar) {
                        bottom.linkTo(parent.bottom)
                    })
            }
        }
    } else {
        ConstraintLayout(modifier = Modifier
            .fillMaxSize()
            .background(SpaceXBlue)) {
            val text = createRef()
            Text(
                text = stringResource(id = R.string.no_data),
                modifier = Modifier.constrainAs(text) {
                    linkTo(
                        top = parent.top,
                        bottom = parent.bottom,
                        start = parent.start,
                        end = parent.end
                    )
                },
                style = MaterialTheme.typography.h1
            )
        }
    }
}