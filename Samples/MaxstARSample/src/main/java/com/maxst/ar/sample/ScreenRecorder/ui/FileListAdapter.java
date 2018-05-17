package com.maxst.ar.sample.ScreenRecorder.ui;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maxst.ar.sample.R;
import com.maxst.ar.sample.databinding.FileViewHolderBinding;

import java.io.File;
import java.util.List;

/**
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 * Created by Charles on 2018. 5. 17..
 */
public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.FileViewHolder> {
	private static final String TAG = FileListAdapter.class.getSimpleName();
	private List<File> files;

	FileClickListener fileClickListener;

	public FileListAdapter(FileClickListener listener) {
		fileClickListener = listener;
	}

	@NonNull
	@Override
	public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		FileViewHolderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.file_view_holder, parent, false);
		FileViewHolder holder = new FileViewHolder(binding);
		binding.getRoot().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fileClickListener.onClick(files.get(holder.getAdapterPosition()));
			}
		});
		return holder;
	}

	@Override
	public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
		holder.binding.setFile(files.get(position));
		holder.binding.executePendingBindings();
	}


	@Override
	public int getItemCount() {
		return files == null ? 0 : files.size();
	}

	public void setFiles(List<File> newFiles) {
		Log.e(TAG, "setFiles: " + newFiles.size());
		if (files == null) {
			files = newFiles;
			notifyItemRangeInserted(0, files.size());
		} else {
			DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
				@Override
				public int getOldListSize() {
					return files.size();
				}

				@Override
				public int getNewListSize() {
					return newFiles.size();
				}

				@Override
				public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
					return files.get(oldItemPosition).getName().equals(newFiles.get(newItemPosition).getName());
				}

				@Override
				public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
					return files.get(oldItemPosition).getName().equals(newFiles.get(newItemPosition).getName());
				}
			});
			files = newFiles;
			result.dispatchUpdatesTo(this);
		}
	}

	public static class FileViewHolder extends RecyclerView.ViewHolder {

		final FileViewHolderBinding binding;

		public FileViewHolder(FileViewHolderBinding binding) {
			super(binding.getRoot());
			this.binding = binding;
		}
	}
}
