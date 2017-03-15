set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x=[1, 2, 3, 4, 5];
y1 = [15644.06943	15481.13157	15628.45303	15805.79765	15943.7891];
y2 = [8024.604633	8074.999338	8247.820376	8430.052228	8616.179027];
y3 = [32.89902434	4.808233314	3.838420616	3.382064558	3.354153721];

p1= semilogy(x, y1, '-k^');
hold on;
p2 = semilogy(x, y2, '-ko');
hold on;
p3 = semilogy(x, y3, '-kd');

xlabel('the number of keywords in S');
ylabel('time (ms)');

axis([0.5 5.5 0.0 40000]);

set(gca, 'xtick', 1:5, 'XTickLabel', {'1','3','5','7','9'});
leg=legend('basic-g-v1','basic-w-v1','SW', 2);
set(leg,'edgecolor','white');

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);