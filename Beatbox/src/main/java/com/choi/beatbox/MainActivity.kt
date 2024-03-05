package com.choi.beatbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.choi.beatbox.databinding.ActivityMainBinding
import com.choi.beatbox.databinding.ListItemSoundBinding
import com.choi.beatbox.sounds.BeatBox
import com.choi.beatbox.sounds.Sound

class MainActivity : AppCompatActivity() {
//    주석 부분이 내가 쓰는 바인딩 방식 (ViewBinding, DataBinding)
//    private lateinit var binding : ActivityMainBinding

    private lateinit var beatBox: BeatBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding=ActivityMainBinding.inflate(layoutInflater).also {
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        beatBox = BeatBox(assets)

        with(binding) {
            recyclerView.apply {
                layoutManager = GridLayoutManager(context, 3)
                adapter = SoundAdapter(beatBox.sounds)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        beatBox.release()
    }

    private inner class SoundHolder(private val binding: ListItemSoundBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.viewModel = SoundViewModel(beatBox)
        }

        fun bind(sound: Sound) {
            binding.apply {
                viewModel?.sound = sound
                // layout 을 즉각 변경 하도록
                executePendingBindings()
            }
        }
    }

    private inner class SoundAdapter(private val sounds: List<Sound>) :
        RecyclerView.Adapter<SoundHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundHolder {
            val binding = DataBindingUtil.inflate<ListItemSoundBinding>(
                layoutInflater,
                R.layout.list_item_sound,
                parent,
                false
            )

            return SoundHolder(binding)
        }

        override fun getItemCount() = sounds.size

        override fun onBindViewHolder(holder: SoundHolder, position: Int) {
            val sound = sounds[position]
            holder.bind(sound)
        }
    }
}
